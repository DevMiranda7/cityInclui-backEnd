package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.owner.CreateOwnerDTO;
import com.gtp.cityinclui.dto.owner.EditOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.entity.Owner;
import com.gtp.cityinclui.entity.PhotoRegister;
import com.gtp.cityinclui.exception.EmailJaExistenteException;
import com.gtp.cityinclui.exception.FormatoArquivoInvalidoException;
import com.gtp.cityinclui.exception.LimiteDeFotosExcedidoException;
import com.gtp.cityinclui.exception.UsuarioNaoExistenteException;
import com.gtp.cityinclui.repository.OwnerRepository;
import com.gtp.cityinclui.repository.PhotoRepository;
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
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final PhotoRepository photoRepository;
    private final CloudStorageService cloudStorageService;
    private final PasswordEncoder passwordEncoder;
    private static final List<MediaType> TIPOS_DE_IMAGEM_PERMITIDO = List.of(
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_PNG
    );
    public OwnerServiceImpl(OwnerRepository ownerRepository, PhotoRepository photoRepository, CloudStorageService cloudStorageService, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.photoRepository = photoRepository;
        this.cloudStorageService = cloudStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<ResponseOwnerDTO> cadastrarAnunciante(CreateOwnerDTO createOwnerDTO, Flux<FilePart> photos) {
        return ownerRepository.existsByEmail(createOwnerDTO.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new EmailJaExistenteException("Email já existe!"));
                    }
                    Owner owner = CreateOwnerDTO.toEntity(createOwnerDTO);
                    owner.setSenha(passwordEncoder.encode(createOwnerDTO.getSenha()));

                    return ownerRepository.save(owner);
                })
                .flatMap(ownerSave ->
                        Mono.zip(
                                Mono.just(ownerSave),
                                this.processarFotos(ownerSave.getId(),photos)
                        )
                            )
                            .map(tuple -> {
                                Owner ownerFinal = tuple.getT1();
                                List<PhotoRegister> fotosSalvas = tuple.getT2();

                                ownerFinal.setFotos(fotosSalvas);
                                return ResponseOwnerDTO.fromEntity(ownerFinal);
                            }
                        );
    }

    @Override
    public Flux<ResponseOwnerDTO> restaurantesCadastrados(){
        return ownerRepository.findAll()
                .concatMap(this::carregarFotosEConverterParaDTO);
    }

    @Override
    public Mono<ResponseOwnerDTO> getPerfilOwner(String email) {
        return ownerRepository.findByEmail(email)
                .flatMap(this::carregarFotosEConverterParaDTO)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsuarioNaoExistenteException("Usuário não encontrado"))));
    }

    @Override
    public Mono<ResponseOwnerDTO> editarAnunciante(String email, EditOwnerDTO editOwnerDTO, Flux<FilePart> photos) {
      return ownerRepository.findByEmail(email)
              .switchIfEmpty(Mono.defer(() -> Mono.error(new UsuarioNaoExistenteException("Usuário não encontrado"))))
              .flatMap(ownerExistente -> atualizarCamposDeTextos(ownerExistente,editOwnerDTO))
              .flatMap(ownerAtualizado -> {
                  Mono<List<PhotoRegister>> fotosFinais = orchestradorDeProcessosDeFoto(ownerAtualizado,photos);

                  return Mono.zip(Mono.just(ownerAtualizado), fotosFinais);
              })
              .map(tuple -> {
                  Owner owner = tuple.getT1();
                  List<PhotoRegister> fotos = tuple.getT2();
                  owner.setFotos(fotos);
                  return ResponseOwnerDTO.fromEntity(owner);
              });
    }

    @Override
    public Mono<Void> deletarContaOwner(String email) {
        Mono<Owner> ownerDelete = ownerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        new UsuarioNaoExistenteException("Owner não encontrado com o email: " + email)
                ));
        return ownerDelete.flatMap(owner -> {
            return photoRepository.findByOwnerId(owner.getId()).collectList()
                    .flatMap(photos -> {
                        Mono<Void> deleteS3Files = Flux.fromIterable(photos)
                                .map(PhotoRegister::getUrlFoto)
                                .flatMap(url -> cloudStorageService.deleteFoto(url))
                                .then();

                        Mono<Void> deletePhotos = photoRepository.deleteAll(photos).then(ownerRepository.delete(owner));

                        return deleteS3Files.then(deletePhotos);
             });
        });
    }

    private Mono<Owner> atualizarCamposDeTextos(Owner ownerExistente, EditOwnerDTO editOwnerDTO){
        if (editOwnerDTO.getNomeDoRestaurante() != null){
            ownerExistente.setNomeDoRestaurante(editOwnerDTO.getNomeDoRestaurante());
        }
        if (editOwnerDTO.getNomeDoAnunciante() != null){
            ownerExistente.setNomeDoAnunciante(editOwnerDTO.getNomeDoAnunciante());
        }
        if (editOwnerDTO.getCardapio() != null){
            ownerExistente.setCardapio(editOwnerDTO.getCardapio());
        }
        if (editOwnerDTO.getTelefone() != null){
            ownerExistente.setTelefone(editOwnerDTO.getTelefone());
        }

        return ownerRepository.save(ownerExistente);
    }

    private Mono<List<PhotoRegister>> orchestradorDeProcessosDeFoto(Owner owner, Flux<FilePart> photos){
        return photos
                .filter(filePart -> filePart.filename() != null && !filePart.filename()
                        .isEmpty()).collectList()
                        .flatMap(fotosValidas -> {

                            if (fotosValidas.size() > 3) {
                                return Mono.error(new LimiteDeFotosExcedidoException("Limite de fotos ultrapassado"));
                            }

                            if (fotosValidas.isEmpty()){
                                return buscarFotosExistentes(owner.getId());
                            }
                            return salvarNovasERemoverAntigas(owner.getId(),Flux.fromIterable(fotosValidas));
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

    private Mono<ResponseOwnerDTO> carregarFotosEConverterParaDTO(Owner owner){
        return photoRepository.findByOwnerId(owner.getId())
                .collectList()
                .map(fotos -> {
                    owner.setFotos(fotos);
                    return ResponseOwnerDTO.fromEntity(owner);
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
            return Mono.error(new FormatoArquivoInvalidoException("Formato de arquivo inválido. Apenas JPEG e PNG são permitidos."));
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
