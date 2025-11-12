package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.Accessibility;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccessibilityRepository extends ReactiveCrudRepository<Accessibility, Long > {
    Flux<Accessibility> findByOwnerId(Long ownerId);

    Mono<Void> deleteByOwnerId(Long ownerId);
}
