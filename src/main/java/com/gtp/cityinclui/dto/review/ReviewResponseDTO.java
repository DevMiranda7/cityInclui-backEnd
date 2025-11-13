package com.gtp.cityinclui.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gtp.cityinclui.entity.Review;

import java.time.LocalDate;

public class ReviewResponseDTO {

    private Long id;
    private Integer nota;
    private String nomeCliente;
    private String comentario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Long id, Integer nota, String nomeCliente, String comentario, LocalDate data) {
        this.id = id;
        this.nota = nota;
        this.nomeCliente = nomeCliente;
        this.comentario = comentario;
        this.data = data;
    }

    public static ReviewResponseDTO fromEntity(Review review){
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setId(review.getId());
        reviewResponseDTO.setNota(review.getNota());
        reviewResponseDTO.setComentario(review.getComentario());
        reviewResponseDTO.setData(review.getDataDoComentario());
        reviewResponseDTO.setNomeCliente(review.getClientNome());
        return reviewResponseDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
