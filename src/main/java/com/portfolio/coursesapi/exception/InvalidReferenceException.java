package com.portfolio.coursesapi.exception;

/**
 * Se lanza cuando el cuerpo de la peticion referencia un recurso que no
 * existe (por ejemplo, un courseId inexistente al crear un alumno).
 * A diferencia de ResourceNotFoundException, aqui el recurso "no encontrado"
 * no es el de la URL sino uno referenciado dentro del JSON, por lo que se
 * traduce a HTTP 400 (Bad Request) y no a 404.
 */
public class InvalidReferenceException extends RuntimeException {

    public InvalidReferenceException(String message) {
        super(message);
    }
}
