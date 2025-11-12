package com.gtp.cityinclui.dto.owner;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public class UpdateRestaurantOwnerDTO {

    @Size(min = 2, max = 100, message = "O nome do restaurante deve ter entre 2 e 100 caracteres")
    private String nomeDoRestaurante;

    @Size(min = 2, max = 100, message = "O nome do anunciante deve ter entre 2 e 50 caracteres")
    private String nomeDoAnunciante;

    private String cardapio;

    @Size(min = 50, max = 200, message = "A descrição do restaurante deverá ter entre 50 e máximo 200 caracteres")
    private String descricao;

    private List<String> acessibilidades;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
    private String telefone;

    public UpdateRestaurantOwnerDTO() {
    }

    public UpdateRestaurantOwnerDTO(String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String descricao, List<String> acessibilidades, String telefone) {
        this.nomeDoRestaurante = nomeDoRestaurante;
        this.nomeDoAnunciante = nomeDoAnunciante;
        this.cardapio = cardapio;
        this.descricao = descricao;
        this.acessibilidades = acessibilidades;
        this.telefone = telefone;
    }

    public String getNomeDoRestaurante() {
        return nomeDoRestaurante;
    }

    public void setNomeDoRestaurante(String nomeDoRestaurante) {
        this.nomeDoRestaurante = nomeDoRestaurante;
    }

    public String getNomeDoAnunciante() {
        return nomeDoAnunciante;
    }

    public void setNomeDoAnunciante(String nomeDoAnunciante) {
        this.nomeDoAnunciante = nomeDoAnunciante;
    }

    public String getCardapio() {
        return cardapio;
    }

    public void setCardapio(String cardapio) {
        this.cardapio = cardapio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getAcessibilidades() {
        return acessibilidades;
    }

    public void setAcessibilidades(List<String> acessibilidades) {
        this.acessibilidades = acessibilidades;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "EditOwnerDTO{" +
                "nomeDoRestaurante='" + nomeDoRestaurante + '\'' +
                ", nomeDoAnunciante='" + nomeDoAnunciante + '\'' +
                ", cardapio='" + cardapio + '\'' +
                ", descricao='" + descricao + '\'' +
                ", acessibilidades=" + acessibilidades +
                ", telefone='" + telefone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateRestaurantOwnerDTO that = (UpdateRestaurantOwnerDTO) o;
        return Objects.equals(nomeDoRestaurante, that.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, that.nomeDoAnunciante) && Objects.equals(cardapio, that.cardapio) && Objects.equals(descricao, that.descricao) && Objects.equals(acessibilidades, that.acessibilidades) && Objects.equals(telefone, that.telefone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoRestaurante, nomeDoAnunciante, cardapio, descricao, acessibilidades, telefone);
    }
}