package com.portfolio.coursesapi.service.impl;

import com.portfolio.coursesapi.dto.request.StudentCreateRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> findAll(Pageable pageable, Long courseId) {
        Page<Student> page = Optional.ofNullable(courseId)
                .map(id -> studentRepository.findByCourseId(id, pageable))
                .orElseGet(() -> studentRepository.findAll(pageable));
        return page.map(StudentMapper.TO_RESPONSE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findAllNoPaging() {
        return studentRepository.findAll().stream()
                .map(StudentMapper.TO_RESPONSE)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return studentRepository.findById(id)
                .map(StudentMapper.TO_RESPONSE)
                .orElseThrow(() -> notFound(id));
    }

    @Override
    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        String normalizedRut = RutUtils.normalize(request.rut());
        if (studentRepository.existsByRut(normalizedRut)) {
            throw new DuplicateResourceException("Ya existe un alumno con el RUT " + normalizedRut);
        }

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new InvalidReferenceException(
                        "El curso con id " + request.courseId() + " no existe"));

        Student saved = studentRepository.save(StudentMapper.toEntity(request, course));
        return StudentMapper.TO_RESPONSE.apply(saved);
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id).orElseThrow(() -> notFound(id));

        if (request.rut() != null && !request.rut().isBlank()) {
            String normalizedRut = RutUtils.normalize(request.rut());
            studentRepository.findByRut(normalizedRut)
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException("Ya existe un alumno con el RUT " + normalizedRut);
                    });
        }

        Course newCourse = null;
        if (request.courseId() != null) {
            newCourse = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new InvalidReferenceException(
                            "El curso con id " + request.courseId() + " no existe"));
        }

        StudentMapper.applyUpdate(student, request, newCourse);
        return StudentMapper.TO_RESPONSE.apply(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw notFound(id);
        }
        studentRepository.deleteById(id);
    }

    private ResourceNotFoundException notFound(Long id) {
        return new ResourceNotFoundException("Alumno con id " + id + " no fue encontrado");
    }
}
