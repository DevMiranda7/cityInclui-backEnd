package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.Owner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OwnerRepository extends ReactiveCrudRepository<Owner, Long > {

    Mono<Boolean> existsByEmail(String email);

    Mono<Owner> findByEmail(String email);
}
