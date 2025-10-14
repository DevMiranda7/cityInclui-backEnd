package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.owner.CreateOwnerDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.service.OwnerService;
import com.gtp.cityinclui.service.impl.OwnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/cityinclui")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerServiceImpl ownerServiceImpl){
        this.ownerService = ownerServiceImpl;

    }

    @PostMapping("/cadastrar-anunciante")
    Mono<ResponseEntity<ResponseOwnerDTO>> cadastrarAnunciante(
            @RequestPart("owner")  Mono<CreateOwnerDTO> createOwnerDTO,
            @RequestPart("photos") Flux<FilePart> photosFlux ){

        return createOwnerDTO.flatMap(
                ownerDTO -> ownerService.cadastrarAnunciante(ownerDTO,photosFlux)
                        .map(responseOwnerDTO ->
                                ResponseEntity.status(HttpStatus.CREATED)
                                        .body(responseOwnerDTO)));
    }

    @GetMapping("/restaurantes")
    Flux<ResponseOwnerDTO> restaurantesCadastrados(){
       return ownerService.restaurantesCadastrados();
    }

}
