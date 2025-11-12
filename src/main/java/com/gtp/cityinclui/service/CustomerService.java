package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.customer.CreateCustomerDTO;
import com.gtp.cityinclui.dto.customer.UpdateCustomerDTO;
import com.gtp.cityinclui.dto.customer.CustomerResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerResponseDTO> createCustomer(CreateCustomerDTO createCustomerDTO);

    Flux<CustomerResponseDTO> getAllCustomers();

    Mono<CustomerResponseDTO> getCustomerProfile(String email);

    Mono<CustomerResponseDTO> updateCustomer(UpdateCustomerDTO updateCustomerDTO, String email);

    Mono<Void> deleteCustomer(String email);
}

