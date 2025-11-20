package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.customer.CreateCustomerDTO;
import com.gtp.cityinclui.dto.customer.UpdateCustomerDTO;
import com.gtp.cityinclui.dto.customer.CustomerResponseDTO;
import com.gtp.cityinclui.exception.AuthenticationRequiredException;
import com.gtp.cityinclui.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cityinclui")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/cadastrar-cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponseDTO> createCustomer(@RequestBody @Valid CreateCustomerDTO createCustomerDTO){
        return customerService.createCustomer(createCustomerDTO);
    }

    @GetMapping("/exibir-clientes")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CustomerResponseDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/cliente/me")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerResponseDTO> getCustomerById(@AuthenticationPrincipal Mono<String> authenticationEmail){
        return authenticationEmail.switchIfEmpty(Mono.error(new AuthenticationRequiredException("Autenticação necessária")))
                .flatMap(email -> customerService.getCustomerProfile(email));
    }
            
    @PutMapping("/editar-cliente")
    public Mono<CustomerResponseDTO> updateCustomer(
            @RequestBody UpdateCustomerDTO updateCustomerDTO,
            @AuthenticationPrincipal Mono<String> authenticationEmail){
            return authenticationEmail.switchIfEmpty(Mono.error(new AuthenticationRequiredException("Autenticação necessária")))
                    .flatMap(email ->
                        customerService.updateCustomer(updateCustomerDTO,email));
    }

    @DeleteMapping("/delete-conta")
    public Mono<Void> deleteCustomer(@AuthenticationPrincipal Mono<String> authenticationEmail){
        return authenticationEmail.switchIfEmpty(Mono.error(new AuthenticationRequiredException("Autenticação necessária")))
                .flatMap(email -> customerService.deleteCustomer(email));
    }


}
