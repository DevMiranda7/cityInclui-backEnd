package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.owner.CreateOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.entity.Owner;
import com.gtp.cityinclui.entity.PhotoRegister;
import com.gtp.cityinclui.exception.EmailJaExistenteException;
import com.gtp.cityinclui.repository.OwnerRepository;
import com.gtp.cityinclui.repository.PhotoRepository;
import com.gtp.cityinclui.service.CloudStorageService;
import com.gtp.cityinclui.service.OwnerService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

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
                });
    }

    private Mono<List<PhotoRegister>> processarFotos(Long ownerId, Flux<FilePart> photos){
        Flux<PhotoRegister> fotosSalvas = photos
                .flatMap(filePart -> salvarFoto(filePart, ownerId));
        return fotosSalvas.collectList();
    }

    private Mono<PhotoRegister> salvarFoto(FilePart filePart, Long ownerId){
        String nomeArquivo = "reg_" + ownerId + "_" + UUID.randomUUID() + ".jpg";
        String contentTipo = filePart.headers().getContentType().toString();
        String safeContentTipo = (contentTipo != null && !contentTipo.isEmpty()) ? contentTipo : "application/octet-stream";

        return toByteArray(filePart)
                .flatMap(bytes ->
        cloudStorageService.uploadFoto(bytes,nomeArquivo,safeContentTipo)
        )
                .flatMap(urlFoto -> {
                    PhotoRegister foto = new PhotoRegister();
                    foto.setOwnerId(ownerId);
                    foto.setUrlFoto(urlFoto);
                    return photoRepository.save(foto);
                });
    }

    private Mono<byte[]> toByteArray(FilePart filePart){
        return filePart.content()
                .reduce(DataBuffer::write)
                .map(DataBuffer::asByteBuffer)
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    return bytes;
                });
    }
}
