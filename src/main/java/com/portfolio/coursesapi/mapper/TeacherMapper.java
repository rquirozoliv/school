package com.portfolio.coursesapi.mapper;

import com.portfolio.coursesapi.dto.request.CourseSummaryDto;
import com.portfolio.coursesapi.dto.request.StudentResponseDto;
import com.portfolio.coursesapi.dto.request.TeacherResponseDto;
import com.portfolio.coursesapi.entity.Student;
import com.portfolio.coursesapi.entity.Teacher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TeacherMapper {

    // Constructor privado para evitar la instanciación de una clase utilitaria
    private TeacherMapper() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada.");
    }

    /**
     * Expresión Lambda funcional e inmutable para transformar un Student (Entidad) a un StudentResponseDto.
     * Previene la recursión cíclica transformando las relaciones pesadas a DTOs planos.
     */
    public static final Function<Teacher, TeacherResponseDto> TO_RESPONSE = teacher -> {
        if (teacher == null) {
            return null;
        }

        // Mapeo defensivo y seguro de la colección de cursos utilizando programación funcional
        Set<CourseSummaryDto> courseDtos = Optional.ofNullable(teacher.getCourses())
                .orElse(Collections.emptySet())
                .stream()
                .map(course -> new CourseSummaryDto(
                        course.getId(),
                        course.getCode(),
                        course.getTitle(),
                        course.getCredits(),
                        course.getDescription()
                ))
                .collect(Collectors.toUnmodifiableSet()); // Garantiza la inmutabilidad de la colección mapeada

        return new TeacherResponseDto(
                teacher.getId(),
                teacher.getRut(),
                teacher.getName(),
                teacher.getEmail(), // Incluido para cumplir con la integridad del JSON de origen
                teacher.getEnrollmentDate(), // Incluido para mantener la consistencia del contrato moderno
                courseDtos
        );
    };
}