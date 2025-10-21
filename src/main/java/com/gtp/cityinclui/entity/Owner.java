package com.gtp.cityinclui.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "owner")
public class Owner {

    @Id
    private Long id;

    @Column("nome_restaurante")
    private String nomeDoRestaurante;

    @Column("nome_anunciante")
    private String nomeDoAnunciante;

    private String cardapio;

    private String email;

    private String telefone;

    private String senha;

    @Transient
    private List<PhotoRegister> fotos;

    @Column("token_recuperacao")
    private String tokenRecuperacao;

    @Column("token_expiracao")
    private LocalDateTime tokenDeExpiracao;
    @CreatedDate
    @Column("data_cadastro")
    private LocalDateTime dataDeCadastro;

    public Owner() {
    }

    public Owner(Long id, String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String email, String telefone, String senha, List<PhotoRegister> fotos) {
        this.id = id;
        this.nomeDoRestaurante = nomeDoRestaurante;
        this.nomeDoAnunciante = nomeDoAnunciante;
        this.cardapio = cardapio;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.fotos = fotos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<PhotoRegister> getFotos() {
        return fotos;
    }

    public void setFotos(List<PhotoRegister> fotos) {
        this.fotos = fotos;
    }

    public LocalDateTime getDataDeCadastro() {
        return dataDeCadastro;
    }

    public void setDataDeCadastro(LocalDateTime dataDeCadastro) {
        this.dataDeCadastro = dataDeCadastro;
    }

    public String getTokenRecuperacao() {
        return tokenRecuperacao;
    }

    public void setTokenRecuperacao(String tokenRecuperacao) {
        this.tokenRecuperacao = tokenRecuperacao;
    }

    public LocalDateTime getTokenDeExpiracao() {
        return tokenDeExpiracao;
    }

    public void setTokenDeExpiracao(LocalDateTime tokenDeExpiracao) {
        this.tokenDeExpiracao = tokenDeExpiracao;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", nomeDoRestaurante='" + nomeDoRestaurante + '\'' +
                ", nomeDoAnunciante='" + nomeDoAnunciante + '\'' +
                ", cardapio=" + cardapio +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", senha='" + senha + '\'' +
                ", fotos=" + fotos +
                ", tokenRecuperacao='" + tokenRecuperacao + '\'' +
                ", tokenDeExpiracao=" + tokenDeExpiracao +
                ", dataDeCadastro=" + dataDeCadastro +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return Objects.equals(id, owner.id) && Objects.equals(nomeDoRestaurante, owner.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, owner.nomeDoAnunciante) && Objects.equals(cardapio, owner.cardapio) && Objects.equals(email, owner.email) && Objects.equals(telefone, owner.telefone) && Objects.equals(senha, owner.senha) && Objects.equals(fotos, owner.fotos) && Objects.equals(tokenRecuperacao, owner.tokenRecuperacao) && Objects.equals(tokenDeExpiracao, owner.tokenDeExpiracao) && Objects.equals(dataDeCadastro, owner.dataDeCadastro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeDoRestaurante, nomeDoAnunciante, cardapio, email, telefone, senha, fotos, tokenRecuperacao, tokenDeExpiracao, dataDeCadastro);
    }
}
