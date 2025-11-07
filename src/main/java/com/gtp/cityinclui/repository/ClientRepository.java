package com.gtp.cityinclui.repository;
import com.gtp.cityinclui.entity.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Client> findByEmail(String email);
}