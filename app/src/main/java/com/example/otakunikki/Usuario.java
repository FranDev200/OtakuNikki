package com.example.otakunikki;

import java.util.List;
import java.util.Objects;

public class Usuario {

    private String nombreCompleto;
    private String userName;
    private String email;
    private String pwd;
    private String region;
    List<Perfil> listaPerfiles;

    public Usuario(String nombreCompleto, String userName, String email, String pwd, String region, List<Perfil> listaPerfiles) {
        setNombreCompleto(nombreCompleto);
        setUserName(userName);
        setEmail(email);
        setPwd(pwd);
        setRegion(region);
        setListaPerfiles(listaPerfiles);
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List<Perfil> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(List<Perfil> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getNombreCompleto(), usuario.getNombreCompleto()) && Objects.equals(getUserName(), usuario.getUserName()) && Objects.equals(getEmail(), usuario.getEmail()) && Objects.equals(getPwd(), usuario.getPwd()) && Objects.equals(getRegion(), usuario.getRegion()) && Objects.equals(getListaPerfiles(), usuario.getListaPerfiles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombreCompleto(), getUserName(), getEmail(), getPwd(), getRegion(), getListaPerfiles());
    }
}
