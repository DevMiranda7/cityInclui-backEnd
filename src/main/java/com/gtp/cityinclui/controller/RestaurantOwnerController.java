package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.owner.CreateRestaurantOwnerDTO;
import com.gtp.cityinclui.dto.owner.UpdateRestaurantOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.exception.AuthenticationRequiredException;
import com.gtp.cityinclui.service.RestaurantOwnerService;
import jakarta.validation.Valid;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/cityinclui")
public class RestaurantOwnerController {

    private final RestaurantOwnerService restaurantOwnerService;

    public RestaurantOwnerController(RestaurantOwnerService restaurantOwnerService){
        this.restaurantOwnerService = restaurantOwnerService;

    }

    @PostMapping("/cadastrar-anunciante")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<ResponseOwnerDTO> createOwner(
            @RequestPart("owner") @Valid CreateRestaurantOwnerDTO createRestaurantOwnerDTO,
            @RequestPart("photos") Flux<FilePart> photosFlux ){

        return restaurantOwnerService.registerOwner(createRestaurantOwnerDTO,photosFlux);
    }

    @GetMapping("/restaurantes")
    Flux<ResponseOwnerDTO> getAllOwners(){
       return restaurantOwnerService.getAllOwners();
    }

    @GetMapping("/restaurante/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ResponseOwnerDTO> getOwnerById(@PathVariable Long ownerId){
        return restaurantOwnerService.getOwnerById(ownerId);
    }

    @GetMapping("/restaurantes/top5")
    public Flux<ResponseOwnerDTO> getTop5Restaurants(){
        return restaurantOwnerService.getTop5RatedOwners();
    }

    @GetMapping("/perfil-anunciante")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseOwnerDTO> getAuthenticatedOwnerProfile(@AuthenticationPrincipal Mono<String> authenticationEmail){
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AuthenticationRequiredException("Autenticação necessária"))
                )
                .flatMap(restaurantOwnerService::getOwnerProfile);
    }

    @PutMapping("/editar-perfil")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseOwnerDTO> updateOwnerProfile(
            @AuthenticationPrincipal Mono<String> authenticationEmail,
            @RequestPart("owner") @Valid UpdateRestaurantOwnerDTO updateRestaurantOwnerDTO,
            @RequestPart("photos") Flux<FilePart> photosFlux
    ){
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AuthenticationRequiredException("Autenticação necessária"))
                )
                .flatMap(email -> restaurantOwnerService.updateOwner(email, updateRestaurantOwnerDTO,photosFlux));
    }

    @DeleteMapping("/conta-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteOwnerAccount(
            @AuthenticationPrincipal Mono<String> authenticationEmail
    ) {
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AuthenticationRequiredException("Autenticação necessária"))
                ).flatMap(email ->
                        restaurantOwnerService.deleteOwnerAccount(email));
    }

}
