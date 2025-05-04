package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Objects;

public class HiloForo implements Parcelable {

    private String idHilo;
    private String usuario;
    private String titulo;
    private Timestamp fecha;
    private String comentario;
    private List<HiloForo> respuestas;
    private String idioma;

    public HiloForo(){}

    public HiloForo(String usuario, String titulo, Timestamp fecha, String comentario, List<HiloForo> respuestas, String idioma) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.fecha = fecha;
        this.comentario = comentario;
        this.respuestas = respuestas;
        this.idioma = idioma;
    }
    public HiloForo(String usuario, String titulo, Timestamp fecha, String comentario, String idioma) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.fecha = fecha;
        this.comentario = comentario;
        this.idioma = idioma;
    }

    protected HiloForo(Parcel in) {
        idHilo = in.readString();
        usuario = in.readString();
        titulo = in.readString();
        fecha = in.readParcelable(Timestamp.class.getClassLoader());
        comentario = in.readString();
        respuestas = in.createTypedArrayList(HiloForo.CREATOR);
        idioma = in.readString();
    }

    public static final Creator<HiloForo> CREATOR = new Creator<HiloForo>() {
        @Override
        public HiloForo createFromParcel(Parcel in) {
            return new HiloForo(in);
        }

        @Override
        public HiloForo[] newArray(int size) {
            return new HiloForo[size];
        }
    };

    public String getIdHilo() {
        return idHilo;
    }

    public void setIdHilo(String idHilo) {
        this.idHilo = idHilo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<HiloForo> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<HiloForo> respuestas) {
        this.respuestas = respuestas;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HiloForo)) return false;
        HiloForo hiloForo = (HiloForo) o;
        return Objects.equals(getIdHilo(), hiloForo.getIdHilo()) && Objects.equals(getUsuario(), hiloForo.getUsuario()) && Objects.equals(getTitulo(), hiloForo.getTitulo()) && Objects.equals(getFecha(), hiloForo.getFecha()) && Objects.equals(getComentario(), hiloForo.getComentario()) && Objects.equals(getRespuestas(), hiloForo.getRespuestas()) && Objects.equals(getIdioma(), hiloForo.getIdioma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdHilo(), getUsuario(), getTitulo(), getFecha(), getComentario(), getRespuestas(), getIdioma());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idHilo);
        dest.writeString(usuario);
        dest.writeString(titulo);
        dest.writeParcelable(fecha, flags);
        dest.writeString(comentario);
        dest.writeTypedList(respuestas);
        dest.writeString(idioma);
    }
}
