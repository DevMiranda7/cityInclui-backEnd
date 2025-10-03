package com.gtp.cityinclui.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "foto_registro")
public class PhotoRegister {

    @Id
    private Long id;

    @Column("owner_id")
    private Long ownerId;

    private String urlFoto;

    public PhotoRegister() {
    }

    public PhotoRegister(Long id, Long ownerId, String urlFoto) {
        this.id = id;
        this.ownerId = ownerId;
        this.urlFoto = urlFoto;
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

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "PhotoRegister{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoRegister that = (PhotoRegister) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerId, that.ownerId) && Objects.equals(urlFoto, that.urlFoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, urlFoto);
    }
}
