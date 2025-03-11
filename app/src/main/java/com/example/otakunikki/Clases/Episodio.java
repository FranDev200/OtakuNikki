package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Episodio implements Parcelable {

    private int idEpisodio;
    private String titulo;
    private String sinopsis;
    private String fecha;
    private boolean estaVisto;

    public Episodio(int idEpisodio, String titulo, String sinopsis, String fecha, boolean estaVisto) {
        setIdEpisodio(idEpisodio);
        setTitulo(titulo);
        setSinopsis(sinopsis);
        setFecha(fecha);
        setEstaVisto(estaVisto);
    }

    protected Episodio(Parcel in) {
        idEpisodio = in.readInt();
        titulo = in.readString();
        sinopsis = in.readString();
        fecha = in.readString();
        estaVisto = in.readByte() != 0;
    }

    public static final Creator<Episodio> CREATOR = new Creator<Episodio>() {
        @Override
        public Episodio createFromParcel(Parcel in) {
            return new Episodio(in);
        }

        @Override
        public Episodio[] newArray(int size) {
            return new Episodio[size];
        }
    };

    public boolean isEstaVisto() {
        return estaVisto;
    }

    public void setEstaVisto(boolean estaVisto) {
        this.estaVisto = estaVisto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idEpisodio);
        dest.writeString(titulo);
        dest.writeString(sinopsis);
        dest.writeString(fecha);
        dest.writeByte((byte) (estaVisto ? 1 : 0));
    }
}
