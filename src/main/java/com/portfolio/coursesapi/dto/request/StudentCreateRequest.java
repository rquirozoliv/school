package com.portfolio.coursesapi.dto.request;

import com.portfolio.coursesapi.validation.rut.ValidRut;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentCreateRequest(

        @NotBlank(message = "El RUT es obligatorio")
        @ValidRut(message = "El RUT ingresado no es valido")
        String rut,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String lastname,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 19, message = "El alumno debe ser mayor de 18 anios")
        Integer age,

        @NotNull(message = "El curso es obligatorio")
        Long courseId
) {
}
