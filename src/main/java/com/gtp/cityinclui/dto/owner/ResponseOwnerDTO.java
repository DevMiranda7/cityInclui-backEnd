package com.gtp.cityinclui.dto.owner;

import com.gtp.cityinclui.entity.Owner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResponseOwnerDTO {
    private String nomeDoRestaurante;
    private String nomeDoAnunciante;
    private String cardapio;
    private String email;
    private String telefone;
    private List<Photos> photos;

    public ResponseOwnerDTO() {
    }

    public ResponseOwnerDTO( String nomeDoRestaurante, String nomeDoAnunciante, String cardapio, String email, String telefone, List<Photos> photos) {
        this.nomeDoRestaurante = nomeDoRestaurante;
        this.nomeDoAnunciante = nomeDoAnunciante;
        this.cardapio = cardapio;
        this.email = email;
        this.telefone = telefone;
        this.photos = photos;
    }

    public static ResponseOwnerDTO fromEntity(Owner owner){
        ResponseOwnerDTO responseOwnerDTO = new ResponseOwnerDTO();
        responseOwnerDTO.setNomeDoRestaurante(owner.getNomeDoRestaurante());
        responseOwnerDTO.setNomeDoAnunciante(owner.getNomeDoAnunciante());
        responseOwnerDTO.setCardapio(owner.getCardapio());
        responseOwnerDTO.setEmail(owner.getEmail());
        responseOwnerDTO.setTelefone(owner.getTelefone());

        if (owner.getFotos() != null){
            List<Photos> photosList = owner.getFotos().stream()
                    .map(Photos::fromEntity)
                    .collect(Collectors.toList());

            responseOwnerDTO.setPhotoDTOS(photosList);
        }
        return responseOwnerDTO;
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

    public List<Photos> getPhotoDTOS() {
        return photos;
    }

    public void setPhotoDTOS(List<Photos> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "ResponseOwnerDTO{" +
                "nomeDoRestaurante='" + nomeDoRestaurante + '\'' +
                ", nomeDoAnunciante='" + nomeDoAnunciante + '\'' +
                ", cardapio='" + cardapio + '\'' +
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
        return Objects.equals(nomeDoRestaurante, that.nomeDoRestaurante) && Objects.equals(nomeDoAnunciante, that.nomeDoAnunciante) && Objects.equals(cardapio, that.cardapio) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(photos, that.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeDoRestaurante, nomeDoAnunciante, cardapio, email, telefone, photos);
    }
}
