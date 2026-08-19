package com.portfolio.coursesapi.validation.rut;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RutValidator.class)
public @interface ValidRut {

    String message() default "RUT invalido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
