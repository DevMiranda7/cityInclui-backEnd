package com.gtp.cityinclui.dto.owner;

import com.gtp.cityinclui.entity.PhotoRegister;

import java.util.Objects;

public class RestaurantPhotoDTO {
    private Long id;
    private String urlFoto;

    public RestaurantPhotoDTO() {
    }

    public RestaurantPhotoDTO(Long id, String urlFoto) {
        this.id = id;
        this.urlFoto = urlFoto;
    }

    public static RestaurantPhotoDTO fromEntity(PhotoRegister photoRegister){
        RestaurantPhotoDTO restaurantPhotoDTO = new RestaurantPhotoDTO();
        restaurantPhotoDTO.setId(photoRegister.getId());
        restaurantPhotoDTO.setUrlFoto(photoRegister.getUrlFoto());
        return restaurantPhotoDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "id=" + id +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantPhotoDTO restaurantPhotoDTO = (RestaurantPhotoDTO) o;
        return Objects.equals(id, restaurantPhotoDTO.id) && Objects.equals(urlFoto, restaurantPhotoDTO.urlFoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlFoto);
    }
}
