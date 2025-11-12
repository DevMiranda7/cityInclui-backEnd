package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.review.CreateReviewDTO;
import com.gtp.cityinclui.dto.review.UpdateReviewDTO;
import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {
     Mono<ReviewResponseDTO> createReview(Long ownerId, CreateReviewDTO createReviewDTO, String email);

     Flux<ReviewResponseDTO> getReviewsByOwner(Long ownerId);

     Mono<ReviewResponseDTO> updateReview(Long ownerId, Long avaliacaoId, UpdateReviewDTO updateReviewDTO, String email);

     Mono<Void> deleteReview(Long ownerId, Long avaliacaoId, String email);
}
