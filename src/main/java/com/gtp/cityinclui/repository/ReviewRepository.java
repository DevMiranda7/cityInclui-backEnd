package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, Long > {
    Flux<Review> findByOwnerId(Long ownerId);

    Mono<Boolean> existsByClientIdAndOwnerId(Long clientId, Long ownerId);
}
