package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.owner.CreateRestaurantOwnerDTO;
import com.gtp.cityinclui.dto.owner.UpdateRestaurantOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OwnerService {

    Mono<ResponseOwnerDTO> cadastrarAnunciante(CreateRestaurantOwnerDTO createRestaurantOwnerDTO, Flux<FilePart> photos);

    Flux<ResponseOwnerDTO> restaurantesCadastrados();

    Mono<ResponseOwnerDTO> restauranteCadastradoPerfil(Long ownerId);

    Mono<ResponseOwnerDTO> getPerfilOwner(String email);

    Mono<ResponseOwnerDTO> editarAnunciante(String email, UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO, Flux<FilePart> photos);

    Mono<Void> deletarContaOwner(String email);
}
