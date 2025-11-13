package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import com.gtp.cityinclui.dto.owner.*;
import com.gtp.cityinclui.entity.Accessibility;
import com.gtp.cityinclui.entity.RestaurantOwner;
import com.gtp.cityinclui.entity.PhotoRegister;
import com.gtp.cityinclui.exception.*;
import com.gtp.cityinclui.repository.*;
import com.gtp.cityinclui.service.CloudStorageService;
import com.gtp.cityinclui.service.RestaurantOwnerService;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {

    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final PhotoRepository photoRepository;
    private final ReviewRepository reviewRepository;
    private final AccessibilityRepository accessibilityRepository;
    private final CloudStorageService cloudStorageService;
    private final PasswordEncoder passwordEncoder;
    private static final List<MediaType> ALLOWED_IMAGE_TYPES = List.of(
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_PNG
    );
    public RestaurantOwnerServiceImpl(
            RestaurantOwnerRepository restaurantOwnerRepository,
            PhotoRepository photoRepository,
            AccessibilityRepository accessibilityRepository,
            ReviewRepository reviewRepository,
            CloudStorageService cloudStorageService,
            PasswordEncoder passwordEncoder)
    {
        this.restaurantOwnerRepository = restaurantOwnerRepository;
        this.photoRepository = photoRepository;
        this.accessibilityRepository = accessibilityRepository;
        this.reviewRepository = reviewRepository;
        this.cloudStorageService = cloudStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<ResponseOwnerDTO> registerOwner(CreateRestaurantOwnerDTO createRestaurantOwnerDTO, Flux<FilePart> photos) {

        Flux<FilePart> validPhotos  = photos
                .filter(filePart -> filePart.filename() != null && !filePart.filename().isEmpty());

        return validatePhotoExistence(validPhotos )
                .then(salvarOwner(createRestaurantOwnerDTO))
                .flatMap(savedOwner  -> savePhotosAndAccessibility(savedOwner , createRestaurantOwnerDTO, validPhotos ))
                .flatMap(this::loadRelationshipsAndConvert);
    }

    @Override
    public Flux<ResponseOwnerDTO> getAllOwners(){
        return restaurantOwnerRepository.findAll()
                .concatMap(this::loadRelationshipsAndConvert);
    }

    @Override
    public Mono<ResponseOwnerDTO> getOwnerById(Long ownerId) {
        return restaurantOwnerRepository.findById(ownerId).
                switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .flatMap(this::loadRelationshipsAndConvert);
    }

    @Override
    public Mono<ResponseOwnerDTO> getOwnerProfile(String email) {
        return restaurantOwnerRepository.findByEmail(email)
                .flatMap(this::loadRelationshipsAndConvert)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException("Usuário não encontrado"))));
    }

    @Override
    public Mono<ResponseOwnerDTO> updateOwner(String email, UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO, Flux<FilePart> photos) {
      return restaurantOwnerRepository.findByEmail(email)
              .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException("Usuário não encontrado"))
                      )
              )
              .flatMap(existingOwner  -> updateTextFields(existingOwner , updateRestaurantOwnerDTO)
              )
              .flatMap(updatedOwner  -> {
                  Mono<List<PhotoRegister>> finalPhotos  = orchestratePhotoProcessing(updatedOwner ,photos);
                  Mono<List<Accessibility>> finalAccessibility  = updateAccessibility(
                          updatedOwner ,
                          updateRestaurantOwnerDTO.getAcessibilidades()
                  );
                  return Mono.zip(
                          Mono.just(updatedOwner ),
                          finalPhotos ,
                          finalAccessibility
                  );
              })
              .map(tuple -> {
                  RestaurantOwner restaurantOwner = tuple.getT1();
                  List<PhotoRegister> photo = tuple.getT2();
                  List<Accessibility> accessibilities  = tuple.getT3();

                  restaurantOwner.setFotos(photo);
                  restaurantOwner.setAcessibilidades(accessibilities);
                  return restaurantOwner;
              })
              .flatMap(this::loadRelationshipsAndConvert);
    }

    @Override
    public Mono<Void> deleteOwnerAccount(String email) {
        Mono<RestaurantOwner> ownerDelete = restaurantOwnerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException("Owner não encontrado com o email: " + email)
                ));
        return ownerDelete.flatMap(restaurantOwner -> {
            return photoRepository.findByOwnerId(restaurantOwner.getId()).collectList()
                    .flatMap(photos -> {
                        Mono<Void> deleteS3Files = Flux.fromIterable(photos)
                                .map(PhotoRegister::getUrlFoto)
                                .flatMap(url -> cloudStorageService.deletePhoto(url))
                                .then();

                        Mono<Void> deleteaccessibilities  = accessibilityRepository.deleteByOwnerId(restaurantOwner.getId());

                        Mono<Void> deletePhotos = photoRepository.deleteAll(photos);

                        return Mono.when(deleteS3Files, deleteaccessibilities , deletePhotos)
                                .then(restaurantOwnerRepository.delete(restaurantOwner));
             });
        });
    }

    private Mono<Void> validatePhotoExistence(Flux<FilePart> photos){
        return photos
                .hasElements()
                .flatMap(hasElements  -> {
                    if(!hasElements ){
                        return Mono.error(new PhotoRequiredException("Pelo menos uma foto é obrigatória."));
                    }
                    return Mono.empty();
                });
    }

    private Mono<RestaurantOwner> salvarOwner(CreateRestaurantOwnerDTO createRestaurantOwnerDTO){
        return restaurantOwnerRepository.existsByEmail(createRestaurantOwnerDTO.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new EmailAlreadyExistsException("Endereço de E-mail já tem cadastro no site!"));
                    }
                    RestaurantOwner restaurantOwner = CreateRestaurantOwnerDTO.toEntity(createRestaurantOwnerDTO);
                    restaurantOwner.setSenha(passwordEncoder.encode(createRestaurantOwnerDTO.getSenha()));

                    return restaurantOwnerRepository.save(restaurantOwner);
                });
    }

    private Mono<RestaurantOwner> savePhotosAndAccessibility(RestaurantOwner restaurantOwnerSalvo, CreateRestaurantOwnerDTO createRestaurantOwnerDTO, Flux<FilePart> photos){
        Mono<List<PhotoRegister>> photoMono = this.processPhotos(restaurantOwnerSalvo.getId(), photos);

        Mono<List<Accessibility>> accessibilityMono  = this.salvarAcessibilidade(
                restaurantOwnerSalvo.getId(),
                createRestaurantOwnerDTO.getAcessibilidades()
        );

        return Mono.zip(photoMono,accessibilityMono )
                .map(tuple ->{
                    List<PhotoRegister> finalPhotos = tuple.getT1();
                    List<Accessibility> finalAcess = tuple.getT2();

                    restaurantOwnerSalvo.setFotos(finalPhotos);
                    restaurantOwnerSalvo.setAcessibilidades(finalAcess);

                    return restaurantOwnerSalvo;
                });
    }

    private Mono<List<Accessibility>> salvarAcessibilidade(Long ownerId, List<String> descriptions){
        List<String> list = (descriptions == null) ? List.of() : descriptions;

        return Flux.fromIterable(list)
                .map(desc -> {
                    Accessibility item = new Accessibility();
                    item.setOwnerId(ownerId);
                    item.setAcessibilidades(desc);
                    return item;
                })
                .flatMap(accessibilityRepository::save)
                .collectList();
    }

    private Mono<RestaurantOwner> updateTextFields(RestaurantOwner owner, UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO){
        if (updateRestaurantOwnerDTO.getNomeDoRestaurante() != null){
            owner.setNomeDoRestaurante(updateRestaurantOwnerDTO.getNomeDoRestaurante());
        }
        if (updateRestaurantOwnerDTO.getNomeDoAnunciante() != null){
            owner.setNomeDoAnunciante(updateRestaurantOwnerDTO.getNomeDoAnunciante());
        }
        if (updateRestaurantOwnerDTO.getCardapio() != null){
            owner.setCardapio(updateRestaurantOwnerDTO.getCardapio());
        }
        if (updateRestaurantOwnerDTO.getDescricao() != null){
            owner.setDescricao(updateRestaurantOwnerDTO.getDescricao());
        }
        if (updateRestaurantOwnerDTO.getTelefone() != null){
            owner.setTelefone(updateRestaurantOwnerDTO.getTelefone());
        }


        return restaurantOwnerRepository.save(owner);
    }

    private Mono<List<Accessibility>> updateAccessibility(RestaurantOwner owner, List<String> newAccessibility){

       if (newAccessibility == null){
           return accessibilityRepository.findByOwnerId(owner.getId())
                   .collectList()
                   .defaultIfEmpty(List.of());
       }

       return accessibilityRepository.deleteByOwnerId(owner.getId())
               .then(
                       Flux.fromIterable(newAccessibility)
                               .map(desc -> {
                                   Accessibility item = new Accessibility();
                                   item.setOwnerId(owner.getId());
                                   item.setAcessibilidades(desc);
                                   return item;
                               })
                               .flatMap(accessibilityRepository::save)
                               .collectList()
                               .defaultIfEmpty(List.of())
               );
    }

    private Mono<List<PhotoRegister>> orchestratePhotoProcessing(RestaurantOwner owner, Flux<FilePart> photos){
        return photos
                .filter(filePart -> filePart.filename() != null && !filePart.filename()
                        .isEmpty()).collectList()
                        .flatMap(validPhotos -> {

                            if (validPhotos.size() > 3) {
                                return Mono.error(new PhotoLimitExceededException("Limite de fotos ultrapassado"));
                            }

                            if (validPhotos.isEmpty()){
                                return getExistingPhotos(owner.getId());
                            }
                            return saveNewAndRemoveOld(owner.getId(),Flux.fromIterable(validPhotos));
                        });
    }

    private Mono<List<PhotoRegister>> saveNewAndRemoveOld(Long onwerId, Flux<FilePart> newPhotos){
        Mono<List<PhotoRegister>> savedNew  = this.processPhotos(onwerId,newPhotos).cache();

        return savedNew.flatMap(savedListPhoto  -> {
            if (savedListPhoto.isEmpty()){
                return getExistingPhotos(onwerId);
            }

            Set<Long> idsFotosExistentes = savedListPhoto.stream()
                    .map(PhotoRegister::getId)
                    .collect(Collectors.toSet());

            Mono<Void> deleteOld = photoRepository.findByOwnerId(onwerId)
                    .filter(oldPhoto -> !idsFotosExistentes.contains(oldPhoto.getId()))
                    .flatMap(this::deletePhotoFromCloud)
                    .then();

            return deleteOld .then(Mono.just(savedListPhoto ));
        });
    }

    private Mono<Void> deletePhotoFromCloud(PhotoRegister photo) {
        Mono<Void> deleteS3 = cloudStorageService.deletePhoto(photo.getUrlFoto());
        Mono<Void> deleteDb = photoRepository.deleteById(photo.getId());
        return Mono.when(deleteS3,deleteDb);
    }

    private Mono<List<PhotoRegister>> getExistingPhotos(Long onwerId){
        return photoRepository.findByOwnerId(onwerId).collectList();
    }

    private Mono<ResponseOwnerDTO> loadRelationshipsAndConvert(RestaurantOwner restaurantOwner){
        Mono<List<RestaurantPhotoDTO>> photoMono = photoRepository.findByOwnerId(restaurantOwner.getId())
                .map(RestaurantPhotoDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<List<AccessibilityDTO>> accessibilityMono  = accessibilityRepository.findByOwnerId(restaurantOwner.getId())
                .map(AccessibilityDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<List<ReviewResponseDTO>> reviewMono = reviewRepository.findByOwnerId(restaurantOwner.getId())
                .map(ReviewResponseDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        return Mono.zip(photoMono, accessibilityMono , reviewMono)
                .map(tuple -> {
                    List<RestaurantPhotoDTO> photos = tuple.getT1();
                    List<AccessibilityDTO> accessibilities = tuple.getT2();
                    List<ReviewResponseDTO> reviews = tuple.getT3();

                    ResponseOwnerDTO responseOwnerDTO = ResponseOwnerDTO.fromEntity(restaurantOwner);
                    responseOwnerDTO.setPhotos(photos);
                    responseOwnerDTO.setAcessibilidadeDTOS(accessibilities);
                    responseOwnerDTO.setAvaliacoes(reviews);

                    return  responseOwnerDTO;
                });
    }

    private Mono<List<PhotoRegister>> processPhotos(Long ownerId, Flux<FilePart> photos){
        Flux<PhotoRegister> savePhotos = photos
                .flatMap(filePart -> savePhoto(filePart, ownerId));
        return savePhotos.collectList();
    }

    private Mono<PhotoRegister> savePhoto(FilePart filePart, Long ownerId){

        if (filePart.filename() == null || filePart.filename().isEmpty()){
            return Mono.empty();
        }

        MediaType mediaType = filePart.headers().getContentType();
        if (mediaType == null || !ALLOWED_IMAGE_TYPES.contains(mediaType)){
            return Mono.error(new InvalidFileFormatException("Formato de arquivo inválido. Apenas JPEG e PNG são permitidos."));
        }
        String safeContentTipo = mediaType.toString();

        String original = filePart.filename();
        String extension = original.contains(".") ? original.substring(original.lastIndexOf(".")) : ".jpg";
        String fileName  = "reg_" + ownerId + "_" + UUID.randomUUID() + "." + extension;

        return toByteArray(filePart)
                .flatMap(bytes -> {
                    if (bytes.length == 0) {
                        return Mono.empty();
                    }

                    return cloudStorageService.uploadPhoto(bytes,fileName ,safeContentTipo);
        })
                .flatMap(urlFoto -> {
                    PhotoRegister photo = new PhotoRegister();
                    photo.setOwnerId(ownerId);
                    photo.setUrlFoto(urlFoto);
                    return photoRepository.save(photo);
                });
    }

    private Mono<byte[]> toByteArray(FilePart filePart){
        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });
    }
}
