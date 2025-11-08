package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.PhotoRegister;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository

public interface PhotoRepository extends ReactiveCrudRepository<PhotoRegister, Long > {

    Flux<PhotoRegister> findByOwnerId(Long ownerId);
}
