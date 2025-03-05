package com.example.otakunikki;

import java.util.List;
import java.util.Objects;

public class Anime {

    private int id;
    private String titulo;
    private String synopsis;
    private double puntuacion;
    private String imagenGrande;
    private String imagenMediana;
    private String imagenPequenia;
    private List<Episodio> listaEpisodios;

    public Anime(int id, String titulo, String synopsis, double puntuacion, String imagenGrande, String imagenMediana, String imagenPequenia, List<Episodio> listaEpisodios) {
        this.id = id;
        this.titulo = titulo;
        this.synopsis = synopsis;
        this.puntuacion = puntuacion;
        this.imagenGrande = imagenGrande;
        this.imagenMediana = imagenMediana;
        this.imagenPequenia = imagenPequenia;
        this.listaEpisodios = listaEpisodios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<Episodio> getListaEpisodios() {
        return listaEpisodios;
    }

    public void setListaEpisodios(List<Episodio> listaEpisodios) { this.listaEpisodios = listaEpisodios; }

    public String getImagenGrande() { return imagenGrande; }

    public void setImagenGrande(String imagenGrande) { this.imagenGrande = imagenGrande; }

    public String getImagenMediana() { return imagenMediana; }

    public void setImagenMediana(String imagenMediana) { this.imagenMediana = imagenMediana; }

    public String getImagenPequenia() { return imagenPequenia; }

    public void setImagenPequenia(String imagenPequenia) { this.imagenPequenia = imagenPequenia; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anime anime = (Anime) o;
        return getId() == anime.getId() && Double.compare(getPuntuacion(), anime.getPuntuacion()) == 0 && Objects.equals(getTitulo(), anime.getTitulo()) && Objects.equals(getSynopsis(), anime.getSynopsis()) && Objects.equals(getImagenGrande(), anime.getImagenGrande()) && Objects.equals(getImagenMediana(), anime.getImagenMediana()) && Objects.equals(getImagenPequenia(), anime.getImagenPequenia()) && Objects.equals(getListaEpisodios(), anime.getListaEpisodios());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitulo(), getSynopsis(), getPuntuacion(), getImagenGrande(), getImagenMediana(), getImagenPequenia(), getListaEpisodios());
    }
}
