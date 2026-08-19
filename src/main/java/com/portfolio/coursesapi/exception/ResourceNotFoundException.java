package com.portfolio.coursesapi.exception;

/**
 * Se lanza cuando el recurso identificado en la URL (:id) no existe.
 * El GlobalExceptionHandler la traduce a HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
