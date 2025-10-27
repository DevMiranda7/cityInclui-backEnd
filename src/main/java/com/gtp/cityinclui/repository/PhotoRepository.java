package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.PhotoRegister;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhotoRepository extends ReactiveCrudRepository<PhotoRegister, Long > {

    Flux<PhotoRegister> findByOwnerId(Long ownerId);

    Mono<Void> deleteAllByOwnerId(Long ownerId);
}
