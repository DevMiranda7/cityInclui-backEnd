package com.gtp.cityinclui.controller;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClienteDTO;
import com.gtp.cityinclui.service.ClientService;
import reactor.core.publisher.Flux;
import com.gtp.cityinclui.service.impl.ClienteServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.gtp.cityinclui.dto.client.EditClienteDTO;
import com.gtp.cityinclui.exception.AutenticacaoNecessariaException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/cityinclui")
public class ClientController {

    private final ClientService clientService;
    public ClientController(ClienteServiceImpl clientServiceImpl) {
        this.clientService = clientServiceImpl;
    }
    @PostMapping("/cadastrar-cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseClienteDTO> cadastrarCliente(
            @RequestBody @Valid CreateClienteDTO createClienteDTO
    ) {
        return clientService.cadastrarCliente(createClienteDTO);
    }
    @GetMapping("/clientes")
    public Flux<ResponseClienteDTO> buscarTodosClientes() {
        return clientService.buscarTodosClientes();
    }
    @PutMapping("/perfil-cliente")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseClienteDTO> atualizarCliente(
            @AuthenticationPrincipal Mono<String> authenticationEmail,
            @RequestBody @Valid EditClienteDTO editClienteDTO
    ) {
        return authenticationEmail
                .switchIfEmpty(Mono.error(
                        new AutenticacaoNecessariaException("Autenticação necessária para editar o perfil.")
                ))
                .flatMap(email ->
                        clientService.atualizarCliente(email, editClienteDTO)
                );
    }
}
