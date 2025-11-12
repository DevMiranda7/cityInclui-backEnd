package com.gtp.cityinclui.dto.customer;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UpdateCustomerDTO {

    @Size(min = 3, max = 50, message = "O nome do Cliente deve ter entre 3 e 50 caracteres")
    private String nomeCompleto;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter apenas números (10 ou 11 dígitos)")
    private String telefone;

    public UpdateCustomerDTO() {
    }

    public UpdateCustomerDTO(String nomeCompleto, String telefone) {
        this.nomeCompleto = nomeCompleto;
        this.telefone = telefone;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "EditClienteDTO{" +
                "nomeCompleto='" + nomeCompleto + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateCustomerDTO that = (UpdateCustomerDTO) o;
        return Objects.equals(nomeCompleto, that.nomeCompleto) && Objects.equals(telefone, that.telefone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeCompleto, telefone);
    }
}
