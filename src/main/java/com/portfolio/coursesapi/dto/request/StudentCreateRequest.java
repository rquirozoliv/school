package com.portfolio.coursesapi.dto.request;

import com.portfolio.coursesapi.validation.rut.ValidRut;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record StudentCreateRequest(

        @NotBlank(message = "El RUT es obligatorio")
        @ValidRut(message = "El RUT ingresado no es valido")
        String rut,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El correo para asegurar el contacto o restablecer contraseña")
        String email,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 19, message = "El alumno debe ser > 18")
        Integer age,

        @NotNull(message = "El tiempo que fue añadido el usuario es obligatoria")
        LocalDate enrollmentDate,

        @NotNull(message = "El curso es obligatorio")
        List<Long> courseIds
) {}
