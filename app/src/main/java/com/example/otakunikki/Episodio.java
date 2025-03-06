package com.example.otakunikki;

import java.util.Objects;

public class Episodio {

    private int idEpisodio;
    private String titulo;
    private String sinopsis;
    private String imagen;

    public Episodio(int idEpisodio, String titulo, String sinopsis, String imagen) {
        setIdEpisodio(idEpisodio);
        setTitulo(titulo);
        setSinopsis(sinopsis);
        setImagen(imagen);
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getIdEpisodio() {
        return idEpisodio;
    }

    public void setIdEpisodio(int idEpisodio) {
        this.idEpisodio = idEpisodio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episodio)) return false;
        Episodio episodio = (Episodio) o;
        return getIdEpisodio() == episodio.getIdEpisodio() && Objects.equals(getTitulo(), episodio.getTitulo()) && Objects.equals(getSinopsis(), episodio.getSinopsis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdEpisodio(), getTitulo(), getSinopsis());
    }
}
