package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.customer.CreateCustomerDTO;
import com.gtp.cityinclui.dto.customer.UpdateCustomerDTO;
import com.gtp.cityinclui.dto.customer.CustomerResponseDTO;
import com.gtp.cityinclui.entity.Customer;
import com.gtp.cityinclui.exception.EmailAlreadyExistsException;
import com.gtp.cityinclui.exception.UserNotFoundException;
import com.gtp.cityinclui.repository.CustomerRepository;
import com.gtp.cityinclui.service.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {
   private final CustomerRepository customerRepository;
   private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<CustomerResponseDTO> createCustomer(CreateCustomerDTO createCustomerDTO) {
        return customerRepository.existsByEmail(createCustomerDTO.getEmail())
                .flatMap(emailExists-> {
                    if (emailExists){
                        return Mono.error(new EmailAlreadyExistsException("Endereço de E-mail já tem cadastro no site!"));
                    }

                    Customer customer = CreateCustomerDTO.toEntity(createCustomerDTO);
                    customer.setSenha(passwordEncoder.encode(createCustomerDTO.getSenha()));

                    return customerRepository.save(customer)
                            .map(CustomerResponseDTO::fromEntity);
                });
    }

    @Override
    public Flux<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .map(CustomerResponseDTO::fromEntity);
    }

    @Override
    public Mono<CustomerResponseDTO> getCustomerProfile(String email) {
        return customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .map(CustomerResponseDTO::fromEntity);
    }

    @Override
    public Mono<CustomerResponseDTO> updateCustomer(UpdateCustomerDTO updateCustomerDTO, String email) {
        return customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException("Usuário não encontrado"))))
                        .flatMap(cliente -> {

                            if (updateCustomerDTO.getNomeCompleto() != null){
                                cliente.setNomeCompleto(updateCustomerDTO.getNomeCompleto());
                            }
                            if (updateCustomerDTO.getTelefone() != null){
                                cliente.setTelefone(updateCustomerDTO.getTelefone());
                            }

                            return customerRepository.save(cliente).map(CustomerResponseDTO::fromEntity);
                        });
    }

    @Override
    public Mono<Void> deleteCustomer(String email) {
        return customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuário não encontrado")))
                .flatMap(client -> customerRepository.deleteById(client.getId()));
    }
}
