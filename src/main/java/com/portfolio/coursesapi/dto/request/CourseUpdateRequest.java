package com.portfolio.coursesapi.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * PUT /courses/:id actualiza solo los campos incluidos en el JSON
 * (merge parcial), por eso ningun campo es @NotBlank/@NotNull aqui:
 * las constraints de formato (Size/Pattern) se ignoran automaticamente
 * cuando el valor llega en null, segun la especificacion de Bean Validation.
 */
public record CourseUpdateRequest(

        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @Pattern(regexp = "^[A-Za-z0-9]{1,4}$", message = "El codigo debe tener entre 1 y 4 caracteres alfanumericos")
        String code
) {
}
