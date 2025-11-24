package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.owner.AccessibilityDTO;
import com.gtp.cityinclui.dto.owner.ResponseOwnerDTO;
import com.gtp.cityinclui.dto.owner.RestaurantPhotoDTO;
import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import com.gtp.cityinclui.entity.RestaurantOwner;
import com.gtp.cityinclui.repository.AccessibilityRepository;
import com.gtp.cityinclui.repository.PhotoRepository;
import com.gtp.cityinclui.repository.ReviewRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class RestaurantOwnerAssembler {
    private final PhotoRepository photoRepository;
    private final AccessibilityRepository accessibilityRepository;
    private final ReviewRepository reviewRepository;


    public RestaurantOwnerAssembler(PhotoRepository photoRepository, AccessibilityRepository accessibilityRepository, ReviewRepository reviewRepository) {
        this.photoRepository = photoRepository;
        this.accessibilityRepository = accessibilityRepository;
        this.reviewRepository = reviewRepository;
    }

    public Mono<ResponseOwnerDTO> assembleOwnerDTO(RestaurantOwner restaurantOwner) {
        Mono<List<RestaurantPhotoDTO>> photoMono = photoRepository.findByOwnerId(restaurantOwner.getId())
                .map(RestaurantPhotoDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<List<AccessibilityDTO>> accessibilityMono = accessibilityRepository.findByOwnerId(restaurantOwner.getId())
                .map(AccessibilityDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<List<ReviewResponseDTO>> reviewMono = reviewRepository.findByOwnerId(restaurantOwner.getId())
                .map(ReviewResponseDTO::fromEntity)
                .collectList()
                .defaultIfEmpty(List.of());

        return Mono.zip(photoMono, accessibilityMono, reviewMono)
                .map(tuple -> {
                    List<RestaurantPhotoDTO> photos = tuple.getT1();
                    List<AccessibilityDTO> accessibilities = tuple.getT2();
                    List<ReviewResponseDTO> reviews = tuple.getT3();

                    ResponseOwnerDTO responseOwnerDTO = ResponseOwnerDTO.fromEntity(restaurantOwner);
                    responseOwnerDTO.setPhotos(photos);
                    responseOwnerDTO.setAcessibilidadeDTOS(accessibilities);
                    responseOwnerDTO.setAvaliacoes(reviews);


                    if (!reviews.isEmpty()) {
                        BigDecimal soma = reviews.stream()
                                .map(review -> BigDecimal.valueOf(review.getNota()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        BigDecimal media = soma.divide(
                                BigDecimal.valueOf(reviews.size()),
                                2,
                                RoundingMode.HALF_UP
                        );
                        responseOwnerDTO.setMediaAvaliacao(media);
                    } else {
                        responseOwnerDTO.setMediaAvaliacao(BigDecimal.ZERO);
                    }

                    return responseOwnerDTO;
                });
    }

}
