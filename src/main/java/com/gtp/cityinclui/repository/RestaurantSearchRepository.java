package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.RestaurantOwner;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantSearchRepository extends ReactiveCrudRepository<RestaurantOwner, Long> {
    @Query("""
            SELECT o.*
            FROM owner o
            WHERE o.nome_restaurante ILIKE CONCAT('%', :query, '%')
               OR o.cardapio ILIKE CONCAT('%', :query, '%')
            ORDER BY o.id
            LIMIT :size OFFSET :offset
            """)
    Flux<RestaurantOwner> findAllMatchingUniqueOwners(String query, long size, long offset);

    @Query("""
            SELECT COUNT(o.id)
            FROM owner o
            WHERE o.nome_restaurante ILIKE CONCAT('%', :query, '%')
               OR o.cardapio ILIKE CONCAT('%', :query, '%')
            """)
    Mono<Long> countSearch(String query);
}
