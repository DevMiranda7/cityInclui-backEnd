package com.gtp.cityinclui.dto.review;

import com.gtp.cityinclui.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateReviewDTO {
    @NotNull(message = "O campo 'nota' é obrigatório.")
    @Min(value = 1, message = "A nota deve ser no mínimo 1.")
    @Max(value = 5, message = "A nota deve ser no máximo 5.")
    private Integer nota;

    private String comentario;

    public CreateReviewDTO() {
    }

    public CreateReviewDTO(Integer nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
    }

    public static Review toEntity(CreateReviewDTO createReviewDTO){
        Review review = new Review();

        review.setNota(createReviewDTO.getNota());
        review.setComentario(createReviewDTO.getComentario());

        return review;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "CreateAvaliacao{" +
                "nota=" + nota +
                ", comentario='" + comentario + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateReviewDTO that = (CreateReviewDTO) o;
        return Objects.equals(nota, that.nota) && Objects.equals(comentario, that.comentario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nota, comentario);
    }
}
