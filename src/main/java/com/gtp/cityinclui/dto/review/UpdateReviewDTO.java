package com.gtp.cityinclui.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Objects;

public class UpdateReviewDTO {
    @Min(value = 1, message = "A nota deve ser no mínimo 1.")
    @Max(value = 5, message = "A nota deve ser no máximo 5.")
    private Integer nota;

    private String comentario;

    public UpdateReviewDTO() {
    }

    public UpdateReviewDTO(Integer nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
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
        return "EditAvalicao{" +
                "nota=" + nota +
                ", comentario='" + comentario + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReviewDTO that = (UpdateReviewDTO) o;
        return Objects.equals(nota, that.nota) && Objects.equals(comentario, that.comentario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nota, comentario);
    }
}
