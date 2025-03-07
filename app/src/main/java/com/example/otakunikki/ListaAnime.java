package com.example.otakunikki;

import java.util.List;
import java.util.Objects;

public class ListaAnime {

    private String nombreLista;
    private List<Anime> listaAnimes;
    private int nroAnimes;

    public ListaAnime(String nombreLista, List<Anime> listaAnimes) {
        setNombreLista(nombreLista);
        setListaAnimes(listaAnimes);
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public List<Anime> getListaAnimes() {
        return listaAnimes;
    }

    public void setListaAnimes(List<Anime> listaAnimes) {
        this.listaAnimes = listaAnimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaAnime)) return false;
        ListaAnime that = (ListaAnime) o;
        return Objects.equals(getNombreLista(), that.getNombreLista()) && Objects.equals(getListaAnimes(), that.getListaAnimes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombreLista(), getListaAnimes());
    }
}
