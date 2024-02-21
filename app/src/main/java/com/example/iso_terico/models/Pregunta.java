package com.example.iso_terico.models;

import java.util.ArrayList;

public class Pregunta {
    private String id;
    private String pregunta;
    private String respuesta_correcta;
    private ArrayList<String> respuestas_incorrectas;

    public Pregunta(String id, String pregunta, String respuesta_correcta, ArrayList<String> respuestas_incorrectas) {
        this.id= id;
        this.pregunta = pregunta;
        this.respuesta_correcta = respuesta_correcta;
        this.respuestas_incorrectas = respuestas_incorrectas;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta_correcta() {
        return respuesta_correcta;
    }

    public ArrayList<String> getRespuestas_incorrectas() {
        return respuestas_incorrectas;
    }
    public String getId() {
        return id;
    }
}
