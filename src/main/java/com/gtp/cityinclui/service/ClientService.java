package com.gtp.cityinclui.service;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.EditClienteDTO; // <-- Importe o novo DTO
import com.gtp.cityinclui.dto.client.ResponseClientDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ClientService {
    Mono<ResponseClientDTO> cadastrarCliente(CreateClienteDTO createClienteDTO);
    Flux<ResponseClientDTO> buscarTodosClientes();
    Mono<ResponseClientDTO> atualizarCliente(String email, EditClienteDTO editClienteDTO);
    Mono<Void> deletarContaCliente(String email);
}
