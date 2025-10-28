package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.PhotoRegister;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
public interface PhotoRepository extends ReactiveCrudRepository<PhotoRegister, Long > {

    Flux<PhotoRegister> findByOwnerId(Long ownerId);
}
