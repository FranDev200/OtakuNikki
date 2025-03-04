package com.example.otakunikki;

import java.util.List;
import java.util.Objects;

public class Perfil {

    private String nombrePerfil;
    private List<ListaAnime> listasAnimes;

    public Perfil(String nombrePerfil, List<ListaAnime> listasAnimes) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil)) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(getNombrePerfil(), perfil.getNombrePerfil()) && Objects.equals(getListasAnimes(), perfil.getListasAnimes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombrePerfil(), getListasAnimes());
    }
}
