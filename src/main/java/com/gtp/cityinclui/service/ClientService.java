package com.gtp.cityinclui.service;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.EditClienteDTO; // <-- Importe o novo DTO
import com.gtp.cityinclui.dto.client.ResponseClienteDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ClientService {
    Mono<ResponseClienteDTO> cadastrarCliente(CreateClienteDTO createClienteDTO);
    Flux<ResponseClienteDTO> buscarTodosClientes();
    Mono<ResponseClienteDTO> atualizarCliente(String email, EditClienteDTO editClienteDTO);
}
