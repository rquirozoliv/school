package com.portfolio.coursesapi.dto.response;

import java.time.Instant;
import java.util.Map;

/**
 * Cuerpo estandar para todas las respuestas de error de la API.
 * fieldErrors solo se completa cuando el error proviene de una
 * violacion de validacion de Bean Validation (@Valid).
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
