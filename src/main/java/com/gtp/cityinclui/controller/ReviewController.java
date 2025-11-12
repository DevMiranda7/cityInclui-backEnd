package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.review.CreateReviewDTO;
import com.gtp.cityinclui.dto.review.UpdateReviewDTO;
import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import com.gtp.cityinclui.exception.AuthenticationRequiredException;
import com.gtp.cityinclui.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cityinclui/owner/{ownerId}/avaliacoes")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ReviewResponseDTO> createReview(
            @PathVariable Long ownerId,
            @RequestBody @Valid CreateReviewDTO createAvaliacao,
            @AuthenticationPrincipal Mono<String> emailAuthentication){

        return emailAuthentication
                .switchIfEmpty(Mono.error(new AuthenticationRequiredException("Autenticação necessária")))
                .flatMap(email -> reviewService.createReview(ownerId,createAvaliacao,email));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ReviewResponseDTO> getReviewsByOwner(@PathVariable Long ownerId){
        return reviewService.getReviewsByOwner(ownerId);
    }

    @PutMapping("/{avaliacaoId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReviewResponseDTO> updateReview(
            @PathVariable Long ownerId,
            @PathVariable Long avaliacaoId,
            @RequestBody @Valid UpdateReviewDTO updateReviewDTO,
            @AuthenticationPrincipal Mono<String> emailAuthentication){
        return emailAuthentication.switchIfEmpty(Mono.error(new AuthenticationRequiredException("Autenticação necessária")))
                .flatMap(email -> reviewService.updateReview(ownerId,avaliacaoId, updateReviewDTO,email));
    }

    @DeleteMapping("/{avaliacaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteReview(
            @PathVariable Long ownerId,
            @PathVariable Long avaliacaoId,
            @AuthenticationPrincipal Mono<String> emailAuthentication){
        return emailAuthentication.switchIfEmpty(Mono.error(new AuthenticationRequiredException("Autenticação necessária")))
                .flatMap(email -> reviewService.deleteReview(ownerId,avaliacaoId,email));
    }
}
