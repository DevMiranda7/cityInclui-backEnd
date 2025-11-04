package com.gtp.cityinclui.service;
import com.gtp.cityinclui.dto.client.CreateClienteDTO;
import com.gtp.cityinclui.dto.client.ResponseClientDTO;
import reactor.core.publisher.Mono;
public interface ClientService {
    Mono<ResponseClientDTO> cadastrarCliente(CreateClienteDTO createClienteDTO);
}
