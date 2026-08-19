package com.portfolio.coursesapi.mapper;

import com.portfolio.coursesapi.dto.request.StudentCreateRequest;
import com.portfolio.coursesapi.dto.request.StudentUpdateRequest;
import com.portfolio.coursesapi.dto.response.StudentResponse;
import com.portfolio.coursesapi.entity.Course;
import com.portfolio.coursesapi.entity.Student;
import com.portfolio.coursesapi.validation.rut.RutUtils;

import java.util.Optional;
import java.util.function.Function;

public final class StudentMapper {

    /** Referencia reutilizable como Function<Student, StudentResponse>, apta para Page::map o Stream::map. */
    public static final Function<Student, StudentResponse> TO_RESPONSE =
            student -> new StudentResponse(
                    student.getId(),
                    student.getRut(),
                    student.getName(),
                    student.getLastname(),
                    student.getAge(),
                    CourseMapper.TO_RESPONSE.apply(student.getCourse())
            );

    private StudentMapper() {
        // utility class
    }

    public static Student toEntity(StudentCreateRequest request, Course course) {
        return Student.builder()
                .rut(RutUtils.normalize(request.rut()))
                .name(request.name().trim())
                .lastname(request.lastname().trim())
                .age(request.age())
                .course(course)
                .build();
    }

    /**
     * Aplica sobre 'student' solo los campos presentes en el request.
     * 'newCourse' ya debe venir resuelto (o null si no se solicito cambio de curso).
     */
    public static void applyUpdate(Student student, StudentUpdateRequest request, Course newCourse) {
        Optional.ofNullable(request.rut())
                .filter(rut -> !rut.isBlank())
                .map(RutUtils::normalize)
                .ifPresent(student::setRut);

        Optional.ofNullable(request.name())
                .filter(name -> !name.isBlank())
                .map(String::trim)
                .ifPresent(student::setName);

        Optional.ofNullable(request.lastname())
                .filter(lastname -> !lastname.isBlank())
                .map(String::trim)
                .ifPresent(student::setLastname);

        Optional.ofNullable(request.age())
                .ifPresent(student::setAge);

        Optional.ofNullable(newCourse)
                .ifPresent(student::setCourse);
    }
}
