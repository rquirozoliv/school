package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.dto.request.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherService {
    /**
     * Recupera una lista paginada de profesores, permitiendo filtrar de forma opcional por curso.
     */
    Page<TeacherResponseDto> findAll(Pageable pageable, Long courseId);

    /**
     * Recupera todos los profesores registrados sin aplicar paginación.
     */
    List<TeacherResponseDto> findAllNoPaging();

    /**
     * Obtiene los detalles de un profesor específico a través de su identificador único.
     */
    TeacherResponseDto findById(Long id);

    /**
     * Registra un nuevo profesor en el sistema y le asocia sus cursos iniciales.
     */
    TeacherResponseDto create(TeacherCreateRequest request);

    /**
     * Actualiza los datos de un profesor existente y refresca sus inscripciones a cursos.
     */
    TeacherResponseDto update(Long id, TeacherUpdateRequest request);

    /**
     * Elimina un profesor del sistema rompiendo previamente sus vínculos relacionales.
     */
    void delete(Long id);
}