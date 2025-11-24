package com.gtp.cityinclui.repository;

import com.gtp.cityinclui.entity.RestaurantOwner;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository

public interface RestaurantOwnerRepository extends ReactiveCrudRepository<RestaurantOwner, Long > {
    Mono<Boolean> existsByEmail(String email);

    Mono<RestaurantOwner> findByEmail(String email);

    @Query("""
             SELECT o.*, ranking.media_avaliacao
                FROM owner o
                INNER JOIN (
                    SELECT a.owner_id, AVG(a.nota) AS media_avaliacao
                    FROM avaliacao a
                    GROUP BY a.owner_id
                ) ranking ON o.id = ranking.owner_id
                ORDER BY ranking.media_avaliacao DESC
                LIMIT 5
            """)
    Flux<RestaurantOwner> findTop5ByAverageRating();
}
