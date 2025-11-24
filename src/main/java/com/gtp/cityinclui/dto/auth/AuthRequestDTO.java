package com.gtp.cityinclui.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class AuthRequestDTO {

    @NotBlank(message = "O E-mail é obrigatório")
    @Email(message = "O formato do E-mail é inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    private String userType;

    public AuthRequestDTO() {
    }

    public AuthRequestDTO(String email, String senha, String userType) {
        this.email = email;
        this.senha = senha;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "AuthRequestDTO{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRequestDTO that = (AuthRequestDTO) o;
        return Objects.equals(email, that.email) && Objects.equals(senha, that.senha) && Objects.equals(userType, that.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, senha, userType);
    }
}
