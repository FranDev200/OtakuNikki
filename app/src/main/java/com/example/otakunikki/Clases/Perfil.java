package com.example.otakunikki.Clases;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Perfil implements Parcelable {

    private String nombrePerfil;
    private int imagenPerfil;
    private List<ListaAnime> listasAnimes;

    public Perfil(){}

    public Perfil(String nombrePerfil, int imagenPerfil){
        this.nombrePerfil = nombrePerfil;
        this.imagenPerfil = imagenPerfil;
        this.listasAnimes = new ArrayList<>();
    }

    public Perfil(String nombrePerfil, int imagenPerfil, List<ListaAnime> listasAnimes) {
        this.nombrePerfil = nombrePerfil;
        this.imagenPerfil = imagenPerfil;
        this.listasAnimes = listasAnimes;
    }

    protected Perfil(Parcel in) {
        nombrePerfil = in.readString();
        imagenPerfil = in.readInt();
        listasAnimes = in.createTypedArrayList(ListaAnime.CREATOR);
    }

    public static final Creator<Perfil> CREATOR = new Creator<Perfil>() {
        @Override
        public Perfil createFromParcel(Parcel in) {
            return new Perfil(in);
        }

        @Override
        public Perfil[] newArray(int size) {
            return new Perfil[size];
        }
    };

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public int getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(int imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public List<ListaAnime> getListasAnimes() {
        return listasAnimes;
    }

    public void setListasAnimes(List<ListaAnime> listasAnimes) {
        this.listasAnimes = listasAnimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombrePerfil);
        dest.writeInt(imagenPerfil);
        dest.writeTypedList(listasAnimes);
    }
}
