package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import com.gtp.cityinclui.dto.owner.*;
import com.gtp.cityinclui.entity.Accessibility;
import com.gtp.cityinclui.entity.RestaurantOwner;
import com.gtp.cityinclui.entity.PhotoRegister;
import com.gtp.cityinclui.exception.*;
import com.gtp.cityinclui.repository.*;
import com.gtp.cityinclui.service.CloudStorageService;
import com.gtp.cityinclui.service.OwnerService;
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
public class RestaurantOwnerServiceImpl implements OwnerService {

    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final PhotoRepository photoRepository;
    private final ReviewRepository reviewRepository;
    private final AccessibilityRepository accessibilityRepository;
    private final CloudStorageService cloudStorageService;
    private final PasswordEncoder passwordEncoder;
    private static final List<MediaType> TIPOS_DE_IMAGEM_PERMITIDO = List.of(
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_PNG
    );
    public RestaurantOwnerServiceImpl(RestaurantOwnerRepository restaurantOwnerRepository, PhotoRepository photoRepository, AccessibilityRepository accessibilityRepository, ReviewRepository reviewRepository, CloudStorageService cloudStorageService, PasswordEncoder passwordEncoder) {
        this.restaurantOwnerRepository = restaurantOwnerRepository;
        this.photoRepository = photoRepository;
        this.accessibilityRepository = accessibilityRepository;
        this.reviewRepository = reviewRepository;
        this.cloudStorageService = cloudStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<ResponseOwnerDTO> cadastrarAnunciante(CreateRestaurantOwnerDTO createRestaurantOwnerDTO, Flux<FilePart> photos) {

        Flux<FilePart> fotosReais = photos
                .filter(filePart -> filePart.filename() != null && !filePart.filename().isEmpty());

        return validarExistenciaFotos(fotosReais)
                .then(salvarOwner(createRestaurantOwnerDTO))
                .flatMap(restaurantOwnerSalvo -> salvarFotosEAcessibilidade(restaurantOwnerSalvo, createRestaurantOwnerDTO, fotosReais))
                .flatMap(this::carregarRelacionamentosEConverter);
    }

    @Override
    public Flux<ResponseOwnerDTO> restaurantesCadastrados(){
        return restaurantOwnerRepository.findAll()
                .concatMap(this::carregarRelacionamentosEConverter);
    }

    @Override
    public Mono<ResponseOwnerDTO> restauranteCadastradoPerfil(Long ownerId) {
        return restaurantOwnerRepository.findById(ownerId).switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .flatMap(this::carregarRelacionamentosEConverter);
    }

    @Override
    public Mono<ResponseOwnerDTO> getPerfilOwner(String email) {
        return restaurantOwnerRepository.findByEmail(email)
                .flatMap(this::carregarRelacionamentosEConverter)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException("Usuário não encontrado"))));
    }

    @Override
    public Mono<ResponseOwnerDTO> editarAnunciante(String email, UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO, Flux<FilePart> photos) {
      return restaurantOwnerRepository.findByEmail(email)
              .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException("Usuário não encontrado"))
                      )
              )
              .flatMap(restaurantOwnerExistente -> atualizarCamposDeTextos(restaurantOwnerExistente, updateRestaurantOwnerDTO)
              )
              .flatMap(restaurantOwnerAtualizado -> {
                  Mono<List<PhotoRegister>> fotosFinais = orchestradorDeProcessosDeFoto(restaurantOwnerAtualizado,photos);

                  Mono<List<Accessibility>> acessMono = atualizarAcessibilidade(
                          restaurantOwnerAtualizado,
                          updateRestaurantOwnerDTO.getAcessibilidades()
                  );
                  return Mono.zip(
                          Mono.just(restaurantOwnerAtualizado),
                          fotosFinais,
                          acessMono
                  );
              })
              .map(tuple -> {
                  RestaurantOwner restaurantOwner = tuple.getT1();
                  List<PhotoRegister> fotos = tuple.getT2();
                  List<Accessibility> acessibilidades = tuple.getT3();

                  restaurantOwner.setFotos(fotos);
                  restaurantOwner.setAcessibilidades(acessibilidades);
                  return restaurantOwner;
              })
              .flatMap(this::carregarRelacionamentosEConverter);
    }

    @Override
    public Mono<Void> deletarContaOwner(String email) {
        Mono<RestaurantOwner> ownerDelete = restaurantOwnerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException("Owner não encontrado com o email: " + email)
                ));
        return ownerDelete.flatMap(restaurantOwner -> {
            return photoRepository.findByOwnerId(restaurantOwner.getId()).collectList()
                    .flatMap(photos -> {
                        Mono<Void> deleteS3Files = Flux.fromIterable(photos)
                                .map(PhotoRegister::getUrlFoto)
                                .flatMap(url -> cloudStorageService.deleteFoto(url))
                                .then();

                        Mono<Void> deleteAcessibilidades = accessibilityRepository.deleteByOwnerId(restaurantOwner.getId());

                        Mono<Void> deletePhotos = photoRepository.deleteAll(photos);

                        return Mono.when(deleteS3Files, deleteAcessibilidades, deletePhotos)
                                .then(restaurantOwnerRepository.delete(restaurantOwner));
             });
        });
    }

    private Mono<Void> validarExistenciaFotos(Flux<FilePart> fotosReais){
        return fotosReais
                .hasElements()
                .flatMap(temElementos -> {
                    if(!temElementos){
                        return Mono.error(new PhotoRequiredException("Pelo menos uma foto é obrigatória."));
                    }

                    return Mono.empty();
                });
    }

    private Mono<RestaurantOwner> salvarOwner(CreateRestaurantOwnerDTO createRestaurantOwnerDTO){
        return restaurantOwnerRepository.existsByEmail(createRestaurantOwnerDTO.getEmail())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new EmailAlreadyExistsException("Endereço de E-mail já tem cadastro no site!"));
                    }
                    RestaurantOwner restaurantOwner = CreateRestaurantOwnerDTO.toEntity(createRestaurantOwnerDTO);
                    restaurantOwner.setSenha(passwordEncoder.encode(createRestaurantOwnerDTO.getSenha()));

                    return restaurantOwnerRepository.save(restaurantOwner);
                });
    }

    private Mono<RestaurantOwner> salvarFotosEAcessibilidade(RestaurantOwner restaurantOwnerSalvo, CreateRestaurantOwnerDTO createRestaurantOwnerDTO, Flux<FilePart> fotosReais){
        Mono<List<PhotoRegister>> fotosMono = this.processarFotos(restaurantOwnerSalvo.getId(), fotosReais);

        Mono<List<Accessibility>> acessMono = this.salvarAcessibilidade(
                restaurantOwnerSalvo.getId(),
                createRestaurantOwnerDTO.getAcessibilidades()
        );

        return Mono.zip(fotosMono,acessMono)
                .map(tuple ->{
                    List<PhotoRegister> fotosSalvas = tuple.getT1();
                    List<Accessibility> acessSalvas = tuple.getT2();

                    restaurantOwnerSalvo.setFotos(fotosSalvas);
                    restaurantOwnerSalvo.setAcessibilidades(acessSalvas);

                    return restaurantOwnerSalvo;
                });
    }

    private Mono<List<Accessibility>> salvarAcessibilidade(Long ownerId, List<String> descricoes){
        List<String> lista = (descricoes == null) ? List.of() : descricoes;

        return Flux.fromIterable(lista)
                .map(descricao -> {
                    Accessibility item = new Accessibility();
                    item.setOwnerId(ownerId);
                    item.setAcessibilidades(descricao);
                    return item;
                })
                .flatMap(accessibilityRepository::save)
                .collectList();
    }

    private Mono<RestaurantOwner> atualizarCamposDeTextos(RestaurantOwner restaurantOwnerExistente, UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO){
        if (updateRestaurantOwnerDTO.getNomeDoRestaurante() != null){
            restaurantOwnerExistente.setNomeDoRestaurante(updateRestaurantOwnerDTO.getNomeDoRestaurante());
        }
        if (updateRestaurantOwnerDTO.getNomeDoAnunciante() != null){
            restaurantOwnerExistente.setNomeDoAnunciante(updateRestaurantOwnerDTO.getNomeDoAnunciante());
        }
        if (updateRestaurantOwnerDTO.getCardapio() != null){
            restaurantOwnerExistente.setCardapio(updateRestaurantOwnerDTO.getCardapio());
        }
        if (updateRestaurantOwnerDTO.getDescricao() != null){
            restaurantOwnerExistente.setDescricao(updateRestaurantOwnerDTO.getDescricao());
        }
        if (updateRestaurantOwnerDTO.getTelefone() != null){
            restaurantOwnerExistente.setTelefone(updateRestaurantOwnerDTO.getTelefone());
        }


        return restaurantOwnerRepository.save(restaurantOwnerExistente);
    }

    private Mono<List<Accessibility>> atualizarAcessibilidade(RestaurantOwner restaurantOwner, List<String> novasAcessibilidades){

       if (novasAcessibilidades == null){
           return accessibilityRepository.findByOwnerId(restaurantOwner.getId())
                   .collectList()
                   .defaultIfEmpty(List.of());
       }

       return accessibilityRepository.deleteByOwnerId(restaurantOwner.getId())
               .then(
                       Flux.fromIterable(novasAcessibilidades)
                               .map(descricaoAcessibilidade -> {
                                   Accessibility item = new Accessibility();
                                   item.setOwnerId(restaurantOwner.getId());
                                   item.setAcessibilidades(descricaoAcessibilidade);
                                   return item;
                               })
                               .flatMap(accessibilityRepository::save)
                               .collectList()
                               .defaultIfEmpty(List.of())
               );
    }

    private Mono<List<PhotoRegister>> orchestradorDeProcessosDeFoto(RestaurantOwner restaurantOwner, Flux<FilePart> photos){
        return photos
                .filter(filePart -> filePart.filename() != null && !filePart.filename()
                        .isEmpty()).collectList()
                        .flatMap(fotosValidas -> {

                            if (fotosValidas.size() > 3) {
                                return Mono.error(new PhotoLimitExceededException("Limite de fotos ultrapassado"));
                            }

                            if (fotosValidas.isEmpty()){
                                return buscarFotosExistentes(restaurantOwner.getId());
                            }
                            return salvarNovasERemoverAntigas(restaurantOwner.getId(),Flux.fromIterable(fotosValidas));
                        });
    }

    private Mono<List<PhotoRegister>> salvarNovasERemoverAntigas(Long onwerId, Flux<FilePart> fotosNovas){
        Mono<List<PhotoRegister>> salvarNovasFotos = this.processarFotos(onwerId,fotosNovas).cache();

        return salvarNovasFotos.flatMap(listaFotosSalvas -> {
            if (listaFotosSalvas.isEmpty()){
                return buscarFotosExistentes(onwerId);
            }

            Set<Long> idsFotosExistentes = listaFotosSalvas.stream()
                    .map(PhotoRegister::getId)
                    .collect(Collectors.toSet());

            Mono<Void> deletarAntigas = photoRepository.findByOwnerId(onwerId)
                    .filter(fotoAntiga -> !idsFotosExistentes.contains(fotoAntiga.getId()))
                    .flatMap(this::deletarFotosS3)
                    .then();

            return deletarAntigas.then(Mono.just(listaFotosSalvas));
        });
    }

    private Mono<Void> deletarFotosS3(PhotoRegister fotoAntigas) {
        Mono<Void> deleteS3 = cloudStorageService.deleteFoto(fotoAntigas.getUrlFoto());
        Mono<Void> deleteDb = photoRepository.deleteById(fotoAntigas.getId());
        return Mono.when(deleteS3,deleteDb);
    }

    private Mono<List<PhotoRegister>> buscarFotosExistentes(Long onwerId){
        return photoRepository.findByOwnerId(onwerId).collectList();
    }

    private Mono<ResponseOwnerDTO> carregarRelacionamentosEConverter(RestaurantOwner restaurantOwner){
        Mono<List<RestaurantPhotoDTO>> fotosMono = photoRepository.findByOwnerId(restaurantOwner.getId())
                .map(RestaurantPhotoDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<List<AccessibilityDTO>> acessibilidadesMono = accessibilityRepository.findByOwnerId(restaurantOwner.getId())
                .map(AccessibilityDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<List<ReviewResponseDTO>> avaliacaoMono = reviewRepository.findByOwnerId(restaurantOwner.getId())
                .map(ReviewResponseDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        return Mono.zip(fotosMono, acessibilidadesMono, avaliacaoMono)
                .map(tuple -> {
                    List<RestaurantPhotoDTO> fotos = tuple.getT1();
                    List<AccessibilityDTO> acessibilidades = tuple.getT2();
                    List<ReviewResponseDTO> avaliacoes = tuple.getT3();

                    ResponseOwnerDTO responseOwnerDTO = ResponseOwnerDTO.fromEntity(restaurantOwner);
                    responseOwnerDTO.setPhotos(fotos);
                    responseOwnerDTO.setAcessibilidadeDTOS(acessibilidades);
                    responseOwnerDTO.setAvaliacoes(avaliacoes);

                    return  responseOwnerDTO;
                });
    }

    private Mono<List<PhotoRegister>> processarFotos(Long ownerId, Flux<FilePart> photos){
        Flux<PhotoRegister> fotosSalvas = photos
                .flatMap(filePart -> salvarFoto(filePart, ownerId));
        return fotosSalvas.collectList();
    }

    private Mono<PhotoRegister> salvarFoto(FilePart filePart, Long ownerId){

        if (filePart.filename() == null || filePart.filename().isEmpty()){
            return Mono.empty();
        }

        MediaType mediaType = filePart.headers().getContentType();
        if (mediaType == null || !TIPOS_DE_IMAGEM_PERMITIDO.contains(mediaType)){
            return Mono.error(new InvalidFileFormatException("Formato de arquivo inválido. Apenas JPEG e PNG são permitidos."));
        }
        String safeContentTipo = mediaType.toString();

        String original = filePart.filename();
        String extensao = original.contains(".") ? original.substring(original.lastIndexOf(".")) : ".jpg";
        String nomeArquivo = "reg_" + ownerId + "_" + UUID.randomUUID() + "." + extensao;

        return toByteArray(filePart)
                .flatMap(bytes -> {
                    if (bytes.length == 0) {
                        return Mono.empty();
                    }

                    return cloudStorageService.uploadFoto(bytes,nomeArquivo,safeContentTipo);
        })
                .flatMap(urlFoto -> {
                    PhotoRegister foto = new PhotoRegister();
                    foto.setOwnerId(ownerId);
                    foto.setUrlFoto(urlFoto);
                    return photoRepository.save(foto);
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
