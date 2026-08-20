package com.portfolio.coursesapi.service.impl;

import com.portfolio.coursesapi.dto.request.CourseSummaryDto;
import com.portfolio.coursesapi.dto.request.StudentCreateRequest;
import com.portfolio.coursesapi.dto.request.StudentResponseDto;
import com.portfolio.coursesapi.dto.request.StudentUpdateRequest;
import com.portfolio.coursesapi.dto.response.StudentResponse;
import com.portfolio.coursesapi.entity.Course;
import com.portfolio.coursesapi.entity.Student;
import com.portfolio.coursesapi.exception.DuplicateResourceException;
import com.portfolio.coursesapi.exception.InvalidReferenceException;
import com.portfolio.coursesapi.exception.ResourceNotFoundException;
import com.portfolio.coursesapi.mapper.StudentMapper;
import com.portfolio.coursesapi.repository.CourseRepository;
import com.portfolio.coursesapi.repository.StudentRepository;
import com.portfolio.coursesapi.service.StudentService;
import com.portfolio.coursesapi.validation.rut.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> findAll(Pageable pageable, Long courseId) {
        Page<Student> page = Optional.ofNullable(courseId)
                .map(id -> studentRepository.findByCourses_Id(id, pageable)) // Busca en la relación ManyToMany
                .orElseGet(() -> studentRepository.findAll(pageable));

        return page.map(StudentMapper.TO_RESPONSE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> findAllNoPaging() {
        return studentRepository.findAll().stream()
                .map(StudentMapper.TO_RESPONSE)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto findById(Long id) {
        return studentRepository.findById(id)
                .map(StudentMapper.TO_RESPONSE)
                .orElseThrow(() -> notFound(id));
    }

    @Override
    @Transactional
    public StudentResponseDto create(StudentCreateRequest request) {
        String normalizedRut = RutUtils.normalize(request.rut());
        if (studentRepository.existsByRut(normalizedRut)) {
            throw new DuplicateResourceException("Ya existe un alumno con el RUT " + normalizedRut);
        }

        if (studentRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Ya existe un alumno con el correo " + request.email());
        }

        Student student = Student.builder()
                .rut(normalizedRut)
                .name(request.name())
                .email(request.email())
                .age(request.age())
                .enrollmentDate(request.enrollmentDate() != null ? request.enrollmentDate() : java.time.LocalDate.now())
                .build();

        if (request.courseIds() != null && !request.courseIds().isEmpty()) {
            request.courseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new InvalidReferenceException("El curso con id " + courseId + " no existe")))
                    .forEach(student::addCourse);
        }

        Student saved = studentRepository.save(student);
        return StudentMapper.TO_RESPONSE.apply(saved);
    }

    @Override
    @Transactional
    public StudentResponseDto update(Long id, StudentUpdateRequest request) {
        // 1. Obtener la entidad administrada o lanzar excepción
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> notFound(id));

        // 2. Validar unicidad del RUT si se intenta modificar
        if (request.rut() != null && !request.rut().isBlank()) {
            String normalizedRut = RutUtils.normalize(request.rut());
            studentRepository.findByRut(normalizedRut)
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException("Ya existe un alumno con el RUT " + normalizedRut);
                    });
            student.setRut(normalizedRut);
        }

        // 3. Actualizar campos básicos condicionales (Incluyendo el apellido de tu Request)
        if (request.name() != null && !request.name().isBlank()) student.setName(request.name());
        if (request.lastname() != null && !request.lastname().isBlank()) student.setLastname(request.lastname());
        if (request.age() != null) student.setAge(request.age());

        // 4. Actualizar Relación de Cursos (Reemplazo seguro del curso único)
        if (request.courseId() != null) {
            // Buscamos el nuevo curso solicitado en la base de datos
            Course newCourse = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new InvalidReferenceException(
                            "El curso con id " + request.courseId() + " no existe"));

            // Copia defensiva en una lista para evitar ConcurrentModificationException al limpiar
            java.util.List<Course> currentCourses = java.util.List.copyOf(student.getCourses());
            currentCourses.forEach(course -> course.getStudents().remove(student));
            student.getCourses().clear();

            // Vinculamos el nuevo curso asignado mediante el método helper bidireccional
            student.addCourse(newCourse);
        }

        // 5. Persistir y retornar el DTO inmutable final
        Student saved = studentRepository.save(student);
        return StudentMapper.TO_RESPONSE.apply(saved);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> notFound(id));

        // Desvincular de forma bidireccional en memoria antes de borrar para evitar violaciones de FK de Hibernate
        student.getCourses().forEach(course -> course.getStudents().remove(student));

        studentRepository.delete(student);
    }

    private ResourceNotFoundException notFound(Long id) {
        return new ResourceNotFoundException("Alumno con id " + id + " no fue encontrado");
    }
}