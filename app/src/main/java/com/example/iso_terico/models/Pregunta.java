package com.example.iso_terico.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L; // Identificador de versión serializable
    private String id;
    private String pregunta;
    private String respuesta_correcta;
    private String explicacion;
    private String inc1;
    private String inc2;
    private String inc3;

    public Pregunta(String id, String pregunta, String respuesta_correcta, String explicacion, String inc1, String inc2, String inc3) {
        this.id = id;
        this.pregunta = pregunta;
        this.respuesta_correcta = respuesta_correcta;
        this.explicacion = explicacion;
        this.inc1 = inc1;
        this.inc2 = inc2;
        this.inc3 = inc3;
    }

    public List<String> getRespustas(){
        List<String> respuestas = new ArrayList<>();
        respuestas.add(respuesta_correcta);
        respuestas.add(inc1);
        if (!inc2.equals("."))
            respuestas.add(inc2);
        else if (!inc3.equals("."))
            respuestas.add(inc3);
        return respuestas;
    }
    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta_correcta() {
        return respuesta_correcta;
    }

    public String getId() {
        return id;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public String getInc1() {
        return inc1;
    }

    public String getInc2() {
        return inc2;
    }

    public String getInc3() {
        return inc3;
    }
}
