package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.service.RestaurantSearchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cityinclui")
public class SearchController {

    RestaurantSearchService searchService;

    public SearchController(RestaurantSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/pesquisa")
    public Mono<Page<ResponseOwnerDTO>> search(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size)
    {
        return searchService.searchRestaurants(query, page, size);
    }
}
