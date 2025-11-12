package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Customer, Long> {

    Mono<Customer> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email);

}
