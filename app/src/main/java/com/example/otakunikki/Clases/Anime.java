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
    private int nroEpisodios;
    private String duracionEp;

    public Anime(){}

    public Anime(int id, String titulo, String synopsis, double puntuacion, String trailer, String imagenGrande,
                 String imagenMediana, String imagenPequenia, List<Episodio> listaEpisodios, List<String> generos,
                 boolean enEmision, int nroEpisodios, String duracionEp) {
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
        this.nroEpisodios = nroEpisodios;
        this.duracionEp = duracionEp;
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
        nroEpisodios = in.readInt();
        duracionEp = in.readString();
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

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getImagenGrande() {
        return imagenGrande;
    }

    public void setImagenGrande(String imagenGrande) {
        this.imagenGrande = imagenGrande;
    }

    public String getImagenMediana() {
        return imagenMediana;
    }

    public void setImagenMediana(String imagenMediana) {
        this.imagenMediana = imagenMediana;
    }

    public String getImagenPequenia() {
        return imagenPequenia;
    }

    public void setImagenPequenia(String imagenPequenia) {
        this.imagenPequenia = imagenPequenia;
    }

    public List<Episodio> getListaEpisodios() {
        return listaEpisodios;
    }

    public void setListaEpisodios(List<Episodio> listaEpisodios) {
        this.listaEpisodios = listaEpisodios;
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

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public int getNroEpisodios() {
        return nroEpisodios;
    }

    public void setNroEpisodios(int nroEpisodios) {
        this.nroEpisodios = nroEpisodios;
    }

    public String getDuracionEp() {
        return duracionEp;
    }

    public void setDuracionEp(String duracionEp) {
        this.duracionEp = duracionEp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Anime)) return false;
        Anime anime = (Anime) o;
        return getId() == anime.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
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
        dest.writeInt(nroEpisodios);
        dest.writeString(duracionEp);
    }
}
