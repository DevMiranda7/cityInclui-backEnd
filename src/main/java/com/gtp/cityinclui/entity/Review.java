package com.gtp.cityinclui.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "avaliacao")
public class Review {

    @Id
    private Long id;

    @Column("owner_id")
    private Long ownerId;

    @Column("client_id")
    private Long clientId;

    @Column("client_nome")
    private String clientNome;

    private Integer nota;

    private String comentario;

    @Column("data_hora")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataDoComentario;

    public Review() {
    }

    public Review(Long id, Long ownerId, Long clientId, String clientNome, Integer nota, String comentario, LocalDate dataDoComentario) {
        this.id = id;
        this.ownerId = ownerId;
        this.clientId = clientId;
        this.clientNome = clientNome;
        this.nota = nota;
        this.comentario = comentario;
        this.dataDoComentario = dataDoComentario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientNome() {
        return clientNome;
    }

    public void setClientNome(String clientNome) {
        this.clientNome = clientNome;
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

    public LocalDate getDataDoComentario() {
        return dataDoComentario;
    }

    public void setDataDoComentario(LocalDate dataDoComentario) {
        this.dataDoComentario = dataDoComentario;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", clientId=" + clientId +
                ", clientNome='" + clientNome + '\'' +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", dataDoComentario=" + dataDoComentario +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(ownerId, review.ownerId) && Objects.equals(clientId, review.clientId) && Objects.equals(clientNome, review.clientNome) && Objects.equals(nota, review.nota) && Objects.equals(comentario, review.comentario) && Objects.equals(dataDoComentario, review.dataDoComentario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, clientId, clientNome, nota, comentario, dataDoComentario);
    }
}
