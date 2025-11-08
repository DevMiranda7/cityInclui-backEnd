package com.gtp.cityinclui.dto.owner;

import com.gtp.cityinclui.entity.Acessibilidades;

import java.util.Objects;

public class AcessibilidadeDTO {
    private Long id;
    private String acessibilidade;

    public AcessibilidadeDTO() {
    }

    public AcessibilidadeDTO(Long id, String acessibilidade) {
        this.id = id;
        this.acessibilidade = acessibilidade;
    }

    public static AcessibilidadeDTO fromEntity(Acessibilidades acessibilidades){
        AcessibilidadeDTO acessibilidadeDTO = new AcessibilidadeDTO();
        acessibilidadeDTO.setId(acessibilidades.getId());
        acessibilidadeDTO.setAcessibilidade(acessibilidades.getAcessibilidades());
        return acessibilidadeDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcessibilidade() {
        return acessibilidade;
    }

    public void setAcessibilidade(String acessibilidade) {
        this.acessibilidade = acessibilidade;
    }

    @Override
    public String toString() {
        return "AcessibilidadeDTO{" +
                "id=" + id +
                ", acessibilidade='" + acessibilidade + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcessibilidadeDTO that = (AcessibilidadeDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(acessibilidade, that.acessibilidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acessibilidade);
    }
}
