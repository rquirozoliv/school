package com.portfolio.coursesapi.mapper;

import com.portfolio.coursesapi.dto.request.CourseCreateRequest;
import com.portfolio.coursesapi.dto.request.CourseUpdateRequest;
import com.portfolio.coursesapi.dto.response.CourseResponse;
import com.portfolio.coursesapi.entity.Course;

import java.util.Optional;
import java.util.function.Function;

public final class CourseMapper {

    /** Referencia reutilizable como Function<Course, CourseResponse>, apta para Page::map o Stream::map. */
    public static final Function<Course, CourseResponse> TO_RESPONSE =
            course -> new CourseResponse(course.getId(), course.getTitle(), course.getCode());

    private CourseMapper() {
        // utility class
    }

    public static Course toEntity(CourseCreateRequest request) {
        return Course.builder()
                .title(request.name().trim())
                .code(normalizeCode(request.code()))
                .build();
    }

    /** Aplica sobre 'course' solo los campos presentes (no nulos ni en blanco) en el request. */
    public static void applyUpdate(Course course, CourseUpdateRequest request) {
        Optional.ofNullable(request.name())
                .filter(name -> !name.isBlank())
                .map(String::trim)
                .ifPresent(course::setTitle);

        Optional.ofNullable(request.code())
                .filter(code -> !code.isBlank())
                .map(CourseMapper::normalizeCode)
                .ifPresent(course::setCode);
    }

    private static String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}
