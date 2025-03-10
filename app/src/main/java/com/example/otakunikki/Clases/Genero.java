package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Genero implements Parcelable {
    int idGenero;
    String nombreGenero;
    int numAnimes; //Esto es el número de animes que son del genero
    String imgGenero;

    /*Constructor completo*/
    public Genero(int idGenero, String nombreGenero, int numAnimes, String urlImagen) {
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
        this.numAnimes = numAnimes;
        this.imgGenero = urlImagen;
    }

    /*Constructor vacío*/
    public Genero() {
        this.idGenero = 0;
        this.nombreGenero = "";
        this.numAnimes = 0;
        this.imgGenero = "";
    }

    protected Genero(Parcel in) {
        idGenero = in.readInt();
        nombreGenero = in.readString();
        numAnimes = in.readInt();
        imgGenero = in.readString();
    }

    public static final Creator<Genero> CREATOR = new Creator<Genero>() {
        @Override
        public Genero createFromParcel(Parcel in) {
            return new Genero(in);
        }

        @Override
        public Genero[] newArray(int size) {
            return new Genero[size];
        }
    };

    /*Getter y Setters*/
    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    public int getNumAnimes() {
        return numAnimes;
    }

    public void setNumAnimes(int numAnimes) {
        this.numAnimes = numAnimes;
    }

    public String getImgGenero() {
        return imgGenero;
    }

    public void setImgGenero(String imgGenero) {
        this.imgGenero = imgGenero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idGenero);
        dest.writeString(nombreGenero);
        dest.writeInt(numAnimes);
        dest.writeString(imgGenero);
    }
}
