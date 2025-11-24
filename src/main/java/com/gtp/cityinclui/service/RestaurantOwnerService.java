package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.owner.CreateRestaurantOwnerDTO;
import com.gtp.cityinclui.dto.owner.UpdateRestaurantOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestaurantOwnerService {

    Mono<ResponseOwnerDTO> registerOwner(CreateRestaurantOwnerDTO createRestaurantOwnerDTO, Flux<FilePart> photos);

    Flux<ResponseOwnerDTO> getAllOwners();

    Flux<ResponseOwnerDTO> getTop5RatedOwners();

    Mono<ResponseOwnerDTO> getOwnerById(Long ownerId);

    Mono<ResponseOwnerDTO> getOwnerProfile(String email);

    Mono<ResponseOwnerDTO> updateOwner(String email, UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO, Flux<FilePart> photos);

    Mono<Void> deleteOwnerAccount(String email);
}
