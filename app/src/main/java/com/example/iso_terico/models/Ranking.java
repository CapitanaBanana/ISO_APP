package com.example.iso_terico.models;

public class Ranking {
    private String tema;
    private String usuario;
    private String puntaje;
    private String id;

    public Ranking(String id, String tema, String usuario, String puntaje) {
        this.id=id;
        this.tema = tema;
        this.usuario = usuario;
        this.puntaje = puntaje;
    }

    public String getId(){return id;}

    public String getTema() {
        return tema;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPuntaje() {
        return puntaje;
    }
}
