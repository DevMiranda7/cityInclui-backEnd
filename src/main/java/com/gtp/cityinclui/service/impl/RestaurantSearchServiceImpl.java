package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.entity.RestaurantOwner;
import com.gtp.cityinclui.repository.AccessibilityRepository;
import com.gtp.cityinclui.repository.PhotoRepository;
import com.gtp.cityinclui.repository.RestaurantSearchRepository;
import com.gtp.cityinclui.repository.ReviewRepository;
import com.gtp.cityinclui.service.RestaurantSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RestaurantSearchServiceImpl implements RestaurantSearchService {
    private final RestaurantSearchRepository restaurantSearchRepository;
    private final ReviewRepository reviewRepository;
    private final AccessibilityRepository accessibilityRepository;
    private final PhotoRepository photoRepository;
    private final RestaurantOwnerAssembler ownerAssembler;
    public RestaurantSearchServiceImpl(RestaurantSearchRepository restaurantSearchRepository, ReviewRepository reviewRepository, AccessibilityRepository accessibilityRepository, PhotoRepository photoRepository, RestaurantOwnerAssembler ownerAssembler) {
        this.restaurantSearchRepository = restaurantSearchRepository;
        this.reviewRepository = reviewRepository;
        this.accessibilityRepository = accessibilityRepository;
        this.photoRepository = photoRepository;
        this.ownerAssembler = ownerAssembler;
    }

    public Mono<Page<ResponseOwnerDTO>> searchRestaurants(String query, int page, int size) {

        long offset = (long) page * size;
        String q = query.trim();

        Mono<Long> totalMono = restaurantSearchRepository.countSearch(q);

        Flux<ResponseOwnerDTO> contentFlux =
                restaurantSearchRepository.findAllMatchingUniqueOwners(q, size, offset)
                        .concatMap(ownerAssembler::assembleOwnerDTO);

        return Mono.zip(totalMono, contentFlux.collectList())
                .map(tuple -> new PageImpl<>(
                        tuple.getT2(),
                        PageRequest.of(page, size),
                        tuple.getT1()
                ));
    }



}
