package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Anime implements Parcelable {

    private int id;
    private String titulo;
    private String synopsis;
    private double puntuacion;
    private String trailer;
    private String imagenGrande;
    private String imagenMediana;
    private String imagenPequenia;
    private List<Episodio> listaEpisodios;
    private List<String> generos;
    private boolean enEmision;
    private boolean favorito = false;

    public Anime(int id, String titulo, String synopsis, double puntuacion, String trailer, String imagenGrande,
                 String imagenMediana, String imagenPequenia, List<Episodio> listaEpisodios, List<String> generos, boolean enEmision) {
        this.id = id;
        this.titulo = titulo;
        this.synopsis = synopsis;
        this.puntuacion = puntuacion;
        this.trailer = trailer;
        this.imagenGrande = imagenGrande;
        this.imagenMediana = imagenMediana;
        this.imagenPequenia = imagenPequenia;
        this.listaEpisodios = listaEpisodios;
        this.generos = generos;
        this.enEmision = enEmision;
        this.favorito = false;
    }


    protected Anime(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        synopsis = in.readString();
        puntuacion = in.readDouble();
        trailer = in.readString();
        imagenGrande = in.readString();
        imagenMediana = in.readString();
        imagenPequenia = in.readString();
        listaEpisodios = in.createTypedArrayList(Episodio.CREATOR);
        generos = in.createStringArrayList();
        enEmision = in.readByte() != 0;
        favorito = in.readByte() != 0;
    }

    public static final Creator<Anime> CREATOR = new Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public boolean isEnEmision() {
        return enEmision;
    }

    public void setEnEmision(boolean enEmision) {
        this.enEmision = enEmision;
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

    public int getNroEpisodios(){
        return getListaEpisodios().size();
    }
    public String getImagenGrande() { return imagenGrande; }

    public void setImagenGrande(String imagenGrande) { this.imagenGrande = imagenGrande; }

    public String getImagenMediana() { return imagenMediana; }

    public void setImagenMediana(String imagenMediana) { this.imagenMediana = imagenMediana; }

    public String getImagenPequenia() { return imagenPequenia; }

    public void setImagenPequenia(String imagenPequenia) { this.imagenPequenia = imagenPequenia; }

    public boolean getFavorito() {return favorito;}

    public void setFavorito(boolean favorito) {this.favorito = favorito;}
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titulo);
        dest.writeString(synopsis);
        dest.writeDouble(puntuacion);
        dest.writeString(trailer);
        dest.writeString(imagenGrande);
        dest.writeString(imagenMediana);
        dest.writeString(imagenPequenia);
        dest.writeTypedList(listaEpisodios);
        dest.writeStringList(generos);
        dest.writeByte((byte) (enEmision ? 1 : 0));
        dest.writeByte((byte) (favorito ? 1 : 0));
    }
}
