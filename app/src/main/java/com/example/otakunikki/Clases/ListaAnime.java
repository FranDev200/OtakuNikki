package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaAnime implements Parcelable {

    private String nombreLista;
    private List<Anime> listaAnimes;
    private int nroAnimes;
    private String fechaModificacion;

    public ListaAnime(){
    }

    public ListaAnime(String nombreLista) {
        this.nombreLista = nombreLista;
        this.listaAnimes = new ArrayList<>();
    }

    public ListaAnime(String nombreLista, List<Anime> listaAnimes) {
        setNombreLista(nombreLista);
        setListaAnimes(listaAnimes);
        setNroAnimes(getListaAnimes().size());
    }

    protected ListaAnime(Parcel in) {
        nombreLista = in.readString();
        listaAnimes = in.createTypedArrayList(Anime.CREATOR);
        nroAnimes = in.readInt();
        fechaModificacion = in.readString();
    }

    public static final Creator<ListaAnime> CREATOR = new Creator<ListaAnime>() {
        @Override
        public ListaAnime createFromParcel(Parcel in) {
            return new ListaAnime(in);
        }

        @Override
        public ListaAnime[] newArray(int size) {
            return new ListaAnime[size];
        }
    };

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public List<Anime> getListaAnimes() {
        return listaAnimes;
    }

    public void setListaAnimes(List<Anime> listaAnimes) {
        this.listaAnimes = listaAnimes;
    }

    public int getNroAnimes() {
        return nroAnimes;
    }

    public void setNroAnimes(int nroAnimes) {
        this.nroAnimes = nroAnimes;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombreLista);
        dest.writeTypedList(listaAnimes);
        dest.writeInt(nroAnimes);
        dest.writeString(fechaModificacion);
    }
}
