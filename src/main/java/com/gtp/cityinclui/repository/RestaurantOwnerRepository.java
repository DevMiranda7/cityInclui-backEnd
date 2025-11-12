package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.RestaurantOwner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository

public interface OwnerRepository extends ReactiveCrudRepository<RestaurantOwner, Long > {

    Mono<Boolean> existsByEmail(String email);

    Mono<RestaurantOwner> findByEmail(String email);
}
