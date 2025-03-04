package com.example.otakunikki;

import java.util.List;

public class Anime {

    private int id;
    private String titulo;
    private String synopsis;
    private double puntuacion;
    private String imagen;
    private List<Episodio> listaEpisodios;

    public Anime(int id, String titulo, String synopsis, double puntuacion, String imagen, List<Episodio> listaEpisodios) {
        this.id = id;
        this.titulo = titulo;
        this.synopsis = synopsis;
        this.puntuacion = puntuacion;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Episodio> getListaEpisodios() {
        return listaEpisodios;
    }

    public void setListaEpisodios(List<Episodio> listaEpisodios) {
        this.listaEpisodios = listaEpisodios;
    }
}
