package com.portfolio.coursesapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseCreateRequest(

        @NotBlank(message = "El nombre del curso es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "El codigo del curso es obligatorio")
        @Pattern(regexp = "^[A-Za-z0-9]{1,4}$", message = "El codigo debe tener entre 1 y 4 caracteres alfanumericos")
        String code
) {
}
