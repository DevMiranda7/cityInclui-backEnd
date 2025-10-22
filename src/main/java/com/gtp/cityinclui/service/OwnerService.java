package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.owner.CreateOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OwnerService {

    Mono<ResponseOwnerDTO> cadastrarAnunciante(CreateOwnerDTO createOwnerDTO, Flux<FilePart> photos);

    Flux<ResponseOwnerDTO> restaurantesCadastrados();

    Mono<ResponseOwnerDTO> getPerfilOwner(String email);
}
