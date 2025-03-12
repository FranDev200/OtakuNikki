package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Perfil implements Parcelable {

    private String nombrePerfil;
    private String imagenPerfil;
    private int imagenPerfilR;
    private List<ListaAnime> listasAnimes;

    public Perfil(){

    }

    public Perfil(String nombrePerfil,int imagenPerfilR){
        this.nombrePerfil = nombrePerfil;
        this.imagenPerfilR = imagenPerfilR;
    }

    public Perfil(String nombrePerfil, String imagenPerfil ){
        this.nombrePerfil = nombrePerfil;
        this.imagenPerfil = imagenPerfil;
    }

    public Perfil(String nombrePerfil, String imagenPerfil,  List<ListaAnime> listasAnimes) {
        this.nombrePerfil = nombrePerfil;
        this.imagenPerfil = imagenPerfil;
        this.listasAnimes = listasAnimes;
    }

    public Perfil(String nombrePerfil,int imagenPerfilR,  List<ListaAnime> listasAnimes) {
        this.nombrePerfil = nombrePerfil;
        this.imagenPerfilR = imagenPerfilR;
        this.listasAnimes = listasAnimes;
    }

    protected Perfil(Parcel in) {
        nombrePerfil = in.readString();
        imagenPerfil = in.readString();
        imagenPerfilR = in.readInt();
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

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public int getImagenPerfilR() {
        return imagenPerfilR;
    }

    public void setImagenPerfilR(int imagenPerfilR) {
        this.imagenPerfilR = imagenPerfilR;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
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
        dest.writeString(imagenPerfil);
        dest.writeInt(imagenPerfilR);
        dest.writeTypedList(listasAnimes);
    }
}
