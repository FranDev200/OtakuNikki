package com.example.otakunikki.Clases;

import java.util.List;
import java.util.Objects;

public class Perfil {

    private String imagenPerfil;
    private String nombrePerfil;
    private List<ListaAnime> listasAnimes;

    public Perfil(String imagenPerfil, String nombrePerfil, List<ListaAnime> listasAnimes) {
        setImagenPerfil(imagenPerfil);
        setNombrePerfil(nombrePerfil);
        setListasAnimes(listasAnimes);
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public List<ListaAnime> getListasAnimes() {
        return listasAnimes;
    }

    public void setListasAnimes(List<ListaAnime> listasAnimes) {
        this.listasAnimes = listasAnimes;
    }

    public String getImagenPerfil() { return imagenPerfil; }

    public void setImagenPerfil(String imagenPerfil) { this.imagenPerfil = imagenPerfil; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(getImagenPerfil(), perfil.getImagenPerfil()) && Objects.equals(getNombrePerfil(), perfil.getNombrePerfil()) && Objects.equals(getListasAnimes(), perfil.getListasAnimes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImagenPerfil(), getNombrePerfil(), getListasAnimes());
    }
}
