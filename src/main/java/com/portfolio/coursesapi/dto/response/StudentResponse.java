package com.portfolio.coursesapi.dto.response;

public record StudentResponse(
        Long id,
        String rut,
        String name,
        String lastname,
        Integer age,
        CourseResponse course
) {
}
