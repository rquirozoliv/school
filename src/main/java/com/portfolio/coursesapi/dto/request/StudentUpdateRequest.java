package com.portfolio.coursesapi.dto.request;

import com.portfolio.coursesapi.validation.rut.ValidRut;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * PUT /students/:id actualiza solo los campos incluidos en el JSON.
 * Igual que en CourseUpdateRequest, se omiten @NotBlank/@NotNull para
 * permitir el merge parcial; cada constraint se salta automaticamente
 * cuando el valor es null.
 */
public record StudentUpdateRequest(

        @ValidRut(message = "El RUT ingresado no es valido")
        String rut,

        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String lastname,

        @Min(value = 19, message = "El alumno debe ser mayor de 18 anios")
        Integer age,

        Long courseId
) {
}
