package com.gtp.cityinclui.dto.owner;

import com.gtp.cityinclui.entity.Accessibility;

import java.util.Objects;

public class AccessibilityDTO {
    private Long id;
    private String acessibilidade;

    public AccessibilityDTO() {
    }

    public AccessibilityDTO(Long id, String acessibilidade) {
        this.id = id;
        this.acessibilidade = acessibilidade;
    }

    public static AccessibilityDTO fromEntity(Accessibility accessibility){
        AccessibilityDTO accessibilityDTO = new AccessibilityDTO();
        accessibilityDTO.setId(accessibility.getId());
        accessibilityDTO.setAcessibilidade(accessibility.getAcessibilidades());
        return accessibilityDTO;
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
        AccessibilityDTO that = (AccessibilityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(acessibilidade, that.acessibilidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acessibilidade);
    }
}
