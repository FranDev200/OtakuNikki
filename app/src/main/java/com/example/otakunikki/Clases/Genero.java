package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Genero implements Parcelable {
    int idGenero;
    String nombreGenero;
    int numAnimes; //Esto es el número de animes que son del genero
    int imgGenero;

    /*Constructor completo*/
    public Genero(int idGenero, String nombreGenero, int numAnimes, int imagen) {
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
        this.numAnimes = numAnimes;
        this.imgGenero = imagen;
    }

    /*Constructor vacío*/
    public Genero() {
    }

    protected Genero(Parcel in) {
        idGenero = in.readInt();
        nombreGenero = in.readString();
        numAnimes = in.readInt();
        imgGenero = in.readInt();
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

    public int getImgGenero() {
        return imgGenero;
    }

    public void setImgGenero(int imgGenero) {
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
        dest.writeInt(imgGenero);
    }
}
