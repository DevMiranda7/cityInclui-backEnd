package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface RestaurantSearchService {
    Mono<Page<ResponseOwnerDTO>> searchRestaurants(String query, int page, int size);
}
