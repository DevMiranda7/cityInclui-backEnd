package com.gtp.cityinclui.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "Acessibilidade")
public class Accessibility {

    @Id
    private Long id;

    @Column("owner_id")
    private Long ownerId;

    @Column("acessibilidades")
    private String acessibilidades;

    public Accessibility() {
    }

    public Accessibility(Long id, Long ownerId, String acessibilidades) {
        this.id = id;
        this.ownerId = ownerId;
        this.acessibilidades = acessibilidades;
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

    public String getAcessibilidades() {
        return acessibilidades;
    }

    public void setAcessibilidades(String acessibilidades) {
        this.acessibilidades = acessibilidades;
    }

    @Override
    public String toString() {
        return "OwnerAcessibilidades{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", acessibilidade='" + acessibilidades + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accessibility that = (Accessibility) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerId, that.ownerId) && Objects.equals(acessibilidades, that.acessibilidades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, acessibilidades);
    }
}
