package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.Acessibilidades;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AcessibilidadesRepository extends ReactiveCrudRepository<Acessibilidades, Long > {
    Flux<Acessibilidades> findByOwnerId(Long ownerId);
    Mono<Void> deleteByOwnerId(Long ownerId);
}
