package com.portfolio.coursesapi.validation.rut;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

/**
 * Valida el formato y digito verificador de un RUT chileno.
 * Un valor null o en blanco se considera valido aqui: la obligatoriedad
 * del campo se delega a @NotBlank, siguiendo la convencion de Bean Validation
 * de que cada constraint valida solo su propia responsabilidad.
 */
public class RutValidator implements ConstraintValidator<ValidRut, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .filter(v -> !v.isBlank())
                .map(RutUtils::isValid)
                .orElse(true);
    }
}
