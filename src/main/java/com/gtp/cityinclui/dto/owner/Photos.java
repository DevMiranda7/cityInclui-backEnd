package com.gtp.cityinclui.dto.owner;

import com.gtp.cityinclui.entity.PhotoRegister;

import java.util.Objects;

public class Photos {

    private Long id;
    private String urlFoto;

    public Photos() {
    }

    public Photos(Long id, String urlFoto) {
        this.id = id;
        this.urlFoto = urlFoto;
    }

    public static Photos fromEntity(PhotoRegister photoRegister){
        Photos photos = new Photos();
        photos.setId(photoRegister.getId());
        photos.setUrlFoto(photoRegister.getUrlFoto());
        return photos;
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
        Photos photos = (Photos) o;
        return Objects.equals(id, photos.id) && Objects.equals(urlFoto, photos.urlFoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlFoto);
    }
}
