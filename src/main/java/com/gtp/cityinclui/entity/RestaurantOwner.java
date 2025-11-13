package com.gtp.cityinclui.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "owner")
public class RestaurantOwner {

    @Id
    private Long id;

    @Column("nome_restaurante")
    private String nomeDoRestaurante;

    @Column("nome_anunciante")
    private String nomeDoAnunciante;

    private String cardapio;

    private String descricao;

    @Transient
    private List<Accessibility> acessibilidades;

    private String email;

    private String telefone;

    private String senha;

    @Transient
    private List<Review> avaliacoes;

    @Transient
    private List<PhotoRegister> fotos;

    @Column("token_recuperacao")
    private String tokenRecuperacao;

    @Column("token_expiracao")
    private LocalDateTime tokenDeExpiracao;
    @CreatedDate
    @Column("data_cadastro")
    private LocalDateTime dataDeCadastro;

    public RestaurantOwner() {
    }

    public RestaurantOwner(Long id, String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String descricao, List<Accessibility> acessibilidades, String email, String telefone, String senha, List<Review> avaliacoes, List<PhotoRegister> fotos) {
        this.id = id;
        this.nomeDoRestaurante = nomeDoRestaurante;
        this.nomeDoAnunciante = nomeDoAnunciante;
        this.cardapio = cardapio;
        this.descricao = descricao;
        this.acessibilidades = acessibilidades;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.avaliacoes = avaliacoes;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Accessibility> getAcessibilidades() {
        return acessibilidades;
    }

    public void setAcessibilidades(List<Accessibility> acessibilidades) {
        this.acessibilidades = acessibilidades;
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

    public List<Review> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Review> avaliacoes) {
        this.avaliacoes = avaliacoes;
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


}
