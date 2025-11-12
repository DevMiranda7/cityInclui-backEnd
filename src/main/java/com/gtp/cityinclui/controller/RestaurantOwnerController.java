package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.owner.CreateOwnerDTO;
import com.gtp.cityinclui.dto.owner.EditOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.exception.AutenticacaoNecessariaException;
import com.gtp.cityinclui.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/cityinclui")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService){
        this.ownerService = ownerService;

    }

    @PostMapping("/cadastrar-anunciante")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<ResponseOwnerDTO> cadastrarAnunciante(
            @RequestPart("owner") @Valid CreateOwnerDTO createOwnerDTO,
            @RequestPart("photos") Flux<FilePart> photosFlux ){

        return ownerService.cadastrarAnunciante(createOwnerDTO,photosFlux);
    }

    @GetMapping("/restaurantes")
    Flux<ResponseOwnerDTO> restaurantesCadastrados(){
       return ownerService.restaurantesCadastrados();
    }

    @GetMapping("/restaurante/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ResponseOwnerDTO> restauranteCadastradoPerfil(@PathVariable Long ownerId){
        return ownerService.restauranteCadastradoPerfil(ownerId);
    }

    @GetMapping("/perfil-anunciante")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseOwnerDTO> buscarPerfilOwner(@AuthenticationPrincipal Mono<String> authenticationEmail){
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AutenticacaoNecessariaException("Autenticação necessária"))
                )
                .flatMap(ownerService::getPerfilOwner);
    }

    @PutMapping("/edit-perfil")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseOwnerDTO> editarAnunciante(
            @AuthenticationPrincipal Mono<String> authenticationEmail,
            @RequestPart("owner") @Valid EditOwnerDTO editOwnerDTO,
            @RequestPart("photos") Flux<FilePart> photosFlux
    ){
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AutenticacaoNecessariaException("Autenticação necessária"))
                )
                .flatMap(email -> ownerService.editarAnunciante(email,editOwnerDTO,photosFlux));
    }

    @DeleteMapping("/advanced-settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletarConta(
            @AuthenticationPrincipal Mono<String> authenticationEmail
    ) {
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AutenticacaoNecessariaException("Autenticação necessária"))
                ).flatMap(email ->
                        ownerService.deletarContaOwner(email));
    }

}
