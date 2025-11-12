package com.gtp.cityinclui.dto.owner;

import com.gtp.cityinclui.dto.review.ReviewResponseDTO;
import com.gtp.cityinclui.entity.RestaurantOwner;

import java.util.List;
import java.util.Objects;

public class ResponseOwnerDTO {
    private Long id;
    private String nomeDoRestaurante;
    private String nomeDoAnunciante;
    private String cardapio;
    private String descricao;
    private String email;
    private String telefone;
    private List<RestaurantPhotoDTO> photos;
    private List<AccessibilityDTO> accessibilityDTOS;
    private List<ReviewResponseDTO> avaliacoes;
    public ResponseOwnerDTO() {
    }

    public ResponseOwnerDTO(Long id, String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String descricao, String email, String telefone, List<RestaurantPhotoDTO> photos, List<AccessibilityDTO> accessibilityDTOS, List<ReviewResponseDTO> avaliacoes) {
        this.id = id;
        this.nomeDoRestaurante = nomeDoRestaurante;
        this.nomeDoAnunciante = nomeDoAnunciante;
        this.cardapio = cardapio;
        this.descricao = descricao;
        this.email = email;
        this.telefone = telefone;
        this.photos = photos;
        this.accessibilityDTOS = accessibilityDTOS;
        this.avaliacoes = avaliacoes;
    }

    public static ResponseOwnerDTO fromEntity(RestaurantOwner restaurantOwner){
        ResponseOwnerDTO responseOwnerDTO = new ResponseOwnerDTO();
        responseOwnerDTO.setId(restaurantOwner.getId());
        responseOwnerDTO.setNomeDoRestaurante(restaurantOwner.getNomeDoRestaurante());
        responseOwnerDTO.setNomeDoAnunciante(restaurantOwner.getNomeDoAnunciante());
        responseOwnerDTO.setCardapio(restaurantOwner.getCardapio());
        responseOwnerDTO.setDescricao(restaurantOwner.getDescricao());
        responseOwnerDTO.setEmail(restaurantOwner.getEmail());
        responseOwnerDTO.setTelefone(restaurantOwner.getTelefone());

        return responseOwnerDTO;
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

    public List<RestaurantPhotoDTO> getPhoto() {
        return photos;
    }

    public void setPhotos(List<RestaurantPhotoDTO> photos) {
        this.photos = photos;
    }

    public List<AccessibilityDTO> getAcessibilidadeDTOS() {
        return accessibilityDTOS;
    }

    public void setAcessibilidadeDTOS(List<AccessibilityDTO> accessibilityDTOS) {
        this.accessibilityDTOS = accessibilityDTOS;
    }

    public List<ReviewResponseDTO> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<ReviewResponseDTO> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    @Override
    public String toString() {
        return "ResponseOwnerDTO{" +
                "nomeDoRestaurante='" + nomeDoRestaurante + '\'' +
                ", nomeDoAnunciante='" + nomeDoAnunciante + '\'' +
                ", cardapio='" + cardapio + '\'' +
                ", descricao='" + descricao + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", photos=" + photos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseOwnerDTO that = (ResponseOwnerDTO) o;
        return Objects.equals(nomeDoRestaurante, that.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, that.nomeDoAnunciante) && Objects.equals(cardapio, that.cardapio) && Objects.equals(descricao, that.descricao) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(photos, that.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoRestaurante, nomeDoAnunciante, cardapio, descricao, email, telefone, photos);
    }
}
