package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.review.CreateReviewDTO;
import com.gtp.cityinclui.dto.review.UpdateReviewDTO;
import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import com.gtp.cityinclui.entity.Review;
import com.gtp.cityinclui.entity.Customer;
import com.gtp.cityinclui.exception.ReviewAlreadyExistsException;
import com.gtp.cityinclui.exception.ReviewNotFoundException;
import com.gtp.cityinclui.exception.InvalidUserException;
import com.gtp.cityinclui.exception.UserNotFoundException;
import com.gtp.cityinclui.repository.ReviewRepository;
import com.gtp.cityinclui.repository.CustomerRepository;
import com.gtp.cityinclui.repository.RestaurantOwnerRepository;
import com.gtp.cityinclui.service.ReviewService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final CustomerRepository customerRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, RestaurantOwnerRepository restaurantOwnerRepository, CustomerRepository customerRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantOwnerRepository = restaurantOwnerRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<ReviewResponseDTO> createReview(Long ownerId, CreateReviewDTO createReviewDTO, String email) {

        Mono<Customer> CustomerMono = customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Cliente não encontrado com o email: " + email)));

        return CustomerMono.flatMap(client -> {

            Mono<Boolean> ownerExistsMono = restaurantOwnerRepository.existsById(ownerId);
            Mono<Boolean> reviewExistsMono = reviewRepository.existsByClientIdAndOwnerId(client.getId(),ownerId);

            return Mono.zip(ownerExistsMono, reviewExistsMono)
                    .flatMap(tuple -> {
                        Boolean ownerExists = tuple.getT1();
                        Boolean reviewExists = tuple.getT2();

                        if (!ownerExists){
                            return Mono.error(new UserNotFoundException("Restaurante não encontrado com ID: "+ownerId));
                        }
                        if (reviewExists){
                            return Mono.error(new ReviewAlreadyExistsException("Você já avaliou este restaurante"));
                        }

                Review newReview = CreateReviewDTO.toEntity(createReviewDTO);
                newReview.setClientId(client.getId());
                newReview.setOwnerId(ownerId);
                newReview.setClientNome(client.getNomeCompleto());
                newReview.setDataDoComentario(LocalDate.now());

                return reviewRepository.save(newReview)
                        .map(ReviewResponseDTO::fromEntity);
                });
            });
    }

    @Override
    public Flux<ReviewResponseDTO> getReviewsByOwner(Long ownerId) {
        return reviewRepository.findByOwnerId(ownerId).map(ReviewResponseDTO::fromEntity);
    }

    @Override
    public Mono<ReviewResponseDTO> updateReview(Long ownerId, Long reviewId, UpdateReviewDTO updateReviewDTO, String email) {
        Mono<Customer> customerMono = customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Cliente não encontrado")));

        Mono<Review> reviewMono = reviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new ReviewNotFoundException("Avaliação não encontrada")));

        return Mono.zip(customerMono,reviewMono)
                .flatMap(tuple -> {
                    Customer customer = tuple.getT1();
                    Review review = tuple.getT2();

                    if (!review.getClientId().equals(customer.getId())){
                        return Mono.error(new InvalidUserException(("Você não tem permissão para editar esta avaliação.")));
                    }

                    boolean modified = false;
                    if (updateReviewDTO.getNota() != null){
                        review.setNota(updateReviewDTO.getNota());
                        modified = true;
                    }
                    if(updateReviewDTO.getComentario() != null){
                        review.setComentario(updateReviewDTO.getComentario());
                        modified = true;
                    }

                    if (modified) {
                        review.setDataDoComentario(LocalDate.now());
                        return reviewRepository.save(review)
                                .map(ReviewResponseDTO::fromEntity);
                    }
                  return Mono.just(ReviewResponseDTO.fromEntity(review));
                });
    }

    @Override
    public Mono<Void> deleteReview(Long ownerId, Long reviewId, String email) {
        Mono<Customer> customerMono = customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Cliente não encontrado")));

        Mono<Review> reviewMono = reviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new ReviewNotFoundException("Avaliação não encontrada")));

        return Mono.zip(customerMono,reviewMono)
                .flatMap(tuple -> {
                    Customer customer = tuple.getT1();
                    Review review = tuple.getT2();

                    if (!review.getClientId().equals(customer.getId())){
                        return Mono.error(new InvalidUserException(("Você não tem permissão para deletar esta avaliação.")));
                    }
                    return reviewRepository.delete(review);
                });
    }
}

