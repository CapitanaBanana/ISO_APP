package com.example.iso_terico.models;

public class PuntajeCalculator {
    // Puntaje base por pregunta
    private static final int PUNTAJE_BASE_POR_PREGUNTA = 11;

    // Tiempo límite para responder una pregunta (en segundos)
    private static final int TIEMPO_LIMITE_POR_PREGUNTA = 20;

    // Bonificación por responder rápido (por segundo)
    private static final double BONIFICACION_POR_SEGUNDO = 1.756;

    // Penalización por tiempo excedido (por segundo)
    private static final double PENALIZACION_POR_SEGUNDO = 0.978;

    public static int calcularPuntajePorPregunta(double tiempoTotal) {
        int puntajePorPregunta = PUNTAJE_BASE_POR_PREGUNTA;

        // Calcular bonificación por tiempo
        int bonificacionPorTiempo = (int) Math.round(Math.max(0, TIEMPO_LIMITE_POR_PREGUNTA - tiempoTotal) * BONIFICACION_POR_SEGUNDO);

        // Calcular penalización por tiempo excedido
        int penalizacionPorTiempoExcedido = (int) Math.round(Math.max(0, tiempoTotal - TIEMPO_LIMITE_POR_PREGUNTA) * PENALIZACION_POR_SEGUNDO);

        // Sumar bonificación y restar penalización al puntaje por pregunta
        puntajePorPregunta += bonificacionPorTiempo;
        puntajePorPregunta -= penalizacionPorTiempoExcedido;

        return puntajePorPregunta;
    }
}