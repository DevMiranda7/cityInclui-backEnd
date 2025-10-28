package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.owner.CreateOwnerDTO;
import com.gtp.cityinclui.dto.owner.EditOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.entity.Owner;
import com.gtp.cityinclui.entity.PhotoRegister;
import com.gtp.cityinclui.exception.EmailJaExistenteException;
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
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final PhotoRepository photoRepository;
    private final CloudStorageService cloudStorageService;
    private final PasswordEncoder passwordEncoder;

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
                .flatMap(this::carregarFotosEConverterParaDTO);
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
               .flatMap(ownerExistente -> {
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
               })
               .flatMap(ownerAtualizado -> {
                   return photos.collectList()
                           .flatMap(listaFilesPart -> {
                               List<FilePart> fotosNovasValidas = listaFilesPart.stream()
                                       .filter(filePart -> filePart.filename() != null && !filePart.filename().isEmpty())
                                       .toList();

                               if (fotosNovasValidas.isEmpty()) {
                                   return photoRepository.findByOwnerId(ownerAtualizado.getId())
                                           .collectList()
                                           .map(fotosAntigasRep -> Tuples.of(ownerAtualizado, fotosAntigasRep));
                               }

                               Flux<FilePart> novasFotos = Flux.fromIterable(fotosNovasValidas);

                               Mono<List<PhotoRegister>> salvarNovasFotos = this.processarFotos(ownerAtualizado.getId(), novasFotos)
                                       .cache();

                               return salvarNovasFotos.flatMap(listaFotosSalvas -> {
                                   if (listaFotosSalvas.isEmpty()) {
                                       return photoRepository.findByOwnerId(ownerAtualizado.getId())
                                               .collectList()
                                               .map(listaFotosAntigas -> Tuples.of(ownerAtualizado, listaFotosAntigas));
                                   }

                                   Set<Long> idsFotosNovas = listaFotosSalvas.stream()
                                           .map(PhotoRegister::getId)
                                           .collect(Collectors.toSet());

                                   Mono<List<PhotoRegister>> fotosAntigas = photoRepository.findByOwnerId(ownerAtualizado.getId())
                                           .filter(foto -> !idsFotosNovas.contains(foto.getId()))
                                           .collectList();

                                   Mono<Void> deletarFotosAntigas = fotosAntigas
                                           .flatMapMany(Flux::fromIterable)
                                           .flatMap(fotosAntiga -> {
                                               Mono<Void> deleteS3 = cloudStorageService.deleteFoto(fotosAntiga.getUrlFoto());

                                               Mono<Void> deleteDb = photoRepository.deleteById(fotosAntiga.getId());
                                               return Mono.when(deleteS3, deleteDb);
                                           })
                                           .then();

                                   return deletarFotosAntigas.then(Mono.just(Tuples.of(ownerAtualizado, listaFotosSalvas)));
                               });
                           });
               })
               .map(tuple -> {
                   Owner owner = tuple.getT1();
                   List<PhotoRegister> novasFotos = tuple.getT2();
                   owner.setFotos(novasFotos);
                   return ResponseOwnerDTO.fromEntity(owner);
               })
               .switchIfEmpty(Mono.defer(() -> Mono.error(new UsuarioNaoExistenteException("Usuário não encontrado"))));
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
        String safeContentTipo = (mediaType == null) ? "application/octet-stream" : mediaType.toString();

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
