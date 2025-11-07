package com.gtp.cityinclui.service;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClienteDTO;
import reactor.core.publisher.Mono;
public interface ClientService {
    Mono<ResponseClienteDTO> cadastrarCliente(CreateClienteDTO createClientDTO);
}
