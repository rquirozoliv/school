package com.portfolio.coursesapi.exception;

/**
 * Se lanza cuando se intenta crear/actualizar un recurso violando una
 * restriccion de unicidad de negocio (codigo de curso o RUT repetido).
 * El GlobalExceptionHandler la traduce a HTTP 409 (Conflict).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
