package com.gtp.cityinclui.dto.owner;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class EditOwnerDTO {

    @Size(min = 2, max = 100, message = "O nome do restaurante deve ter entre 2 e 100 caracteres")
    private String nomeDoRestaurante;

    @Size(min = 2, max = 100, message = "O nome do anunciante deve ter entre 2 e 50 caracteres")
    private String nomeDoAnunciante;

    private String cardapio;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
    private String telefone;

    public EditOwnerDTO() {
    }

    public EditOwnerDTO(String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String telefone) {
        this.nomeDoRestaurante = nomeDoRestaurante;
        this.nomeDoAnunciante = nomeDoAnunciante;
        this.cardapio = cardapio;
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
                ", telefone='" + telefone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditOwnerDTO that = (EditOwnerDTO) o;
        return Objects.equals(nomeDoRestaurante, that.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, that.nomeDoAnunciante) && Objects.equals(cardapio, that.cardapio) && Objects.equals(telefone, that.telefone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoRestaurante, nomeDoAnunciante, cardapio, telefone);
    }
}
