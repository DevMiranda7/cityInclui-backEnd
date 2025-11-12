package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.client.CreateClientDTO;
import com.gtp.cityinclui.dto.client.EditClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClientDTO;
import com.gtp.cityinclui.exception.AutenticacaoNecessariaException;
import com.gtp.cityinclui.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cityinclui")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/cadastrar-cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseClientDTO> cadastrarCliente(@RequestBody @Valid CreateClientDTO createClientDTO){
        return clientService.cadastrarCliente(createClientDTO);
    }

    @GetMapping("/exibir-clientes")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ResponseClientDTO> exibirClientes(){
        return clientService.exibirClientes();
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseClientDTO> exibirPerfilCliente(@AuthenticationPrincipal Mono<String> authenticationEmail){
        return authenticationEmail.switchIfEmpty(Mono.error(new AutenticacaoNecessariaException("Autenticação necessária")))
                .flatMap(email -> clientService.exibirPerfilCliente(email));
    }
            
    @PutMapping("/editar-cliente")
    public Mono<ResponseClientDTO> editarCliente(
            @RequestBody EditClienteDTO editClienteDTO,
            @AuthenticationPrincipal Mono<String> authenticationEmail){
            return authenticationEmail.switchIfEmpty(Mono.error(new AutenticacaoNecessariaException("Autenticação necessária")))
                    .flatMap(email ->
                        clientService.editarCliente(editClienteDTO,email));
    }

    @DeleteMapping(" ")
    public Mono<Void> deletarCliente(@AuthenticationPrincipal Mono<String> authenticationEmail){
        return authenticationEmail.switchIfEmpty(Mono.error(new AutenticacaoNecessariaException("Autenticação necessária")))
                .flatMap(email -> clientService.deletarCliente(email));
    }


}
