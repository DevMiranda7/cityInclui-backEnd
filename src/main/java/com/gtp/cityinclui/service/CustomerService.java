package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.customer.CreateCustomerDTO;
import com.gtp.cityinclui.dto.customer.UpdateCustomerDTO;
import com.gtp.cityinclui.dto.customer.CustomerResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Mono<CustomerResponseDTO> cadastrarCliente(CreateCustomerDTO createCustomerDTO);

    Flux<CustomerResponseDTO> exibirClientes();

    Mono<CustomerResponseDTO> exibirPerfilCliente(String email);

    Mono<CustomerResponseDTO> editarCliente(UpdateCustomerDTO updateCustomerDTO, String email);

    Mono<Void> deletarCliente(String email);
}
