package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.dto.request.StudentCreateRequest;
import com.portfolio.coursesapi.dto.request.StudentResponseDto;
import com.portfolio.coursesapi.dto.request.StudentUpdateRequest;
import com.portfolio.coursesapi.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    /**
     * Recupera una lista paginada de estudiantes, permitiendo filtrar de forma opcional por curso.
     */
    Page<StudentResponseDto> findAll(Pageable pageable, Long courseId);

    /**
     * Recupera todos los estudiantes registrados sin aplicar paginación.
     */
    List<StudentResponseDto> findAllNoPaging();

    /**
     * Obtiene los detalles de un estudiante específico a través de su identificador único.
     */
    StudentResponseDto findById(Long id);

    /**
     * Registra un nuevo estudiante en el sistema y le asocia sus cursos iniciales.
     */
    StudentResponseDto create(StudentCreateRequest request);

    /**
     * Actualiza los datos de un estudiante existente y refresca sus inscripciones a cursos.
     */
    StudentResponseDto update(Long id, StudentUpdateRequest request);

    /**
     * Elimina un estudiante del sistema rompiendo previamente sus vínculos relacionales.
     */
    void delete(Long id);
}