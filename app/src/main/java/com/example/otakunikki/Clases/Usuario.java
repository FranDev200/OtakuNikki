package com.example.otakunikki.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Usuario  implements Parcelable{

    private String idUsuario;
    private String nombreCompleto;
    private String userName;
    private String email;
    private String region;
    private List<Perfil> listaPerfiles;

    public Usuario(){}

    public Usuario(String idUsuario, String nombreCompleto, String userName, String email, String region, List<Perfil> listaPerfiles) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.userName = userName;
        this.email = email;
        this.region = region;
        this.listaPerfiles = listaPerfiles;
    }


    protected Usuario(Parcel in) {
        idUsuario = in.readString();
        nombreCompleto = in.readString();
        userName = in.readString();
        email = in.readString();
        region = in.readString();
        listaPerfiles = in.createTypedArrayList(Perfil.CREATOR);
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Perfil> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(List<Perfil> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getIdUsuario(), usuario.getIdUsuario()) && Objects.equals(getUserName(), usuario.getUserName()) && Objects.equals(getEmail(), usuario.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUsuario(), getUserName(), getEmail());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idUsuario);
        dest.writeString(nombreCompleto);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(region);
        dest.writeTypedList(listaPerfiles);
    }
}
