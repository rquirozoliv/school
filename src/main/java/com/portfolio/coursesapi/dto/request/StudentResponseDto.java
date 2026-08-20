package com.portfolio.coursesapi.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record StudentResponseDto(
        Long id,
        String rut,
        String name,
        String email,
        Integer age,
        LocalDate enrollmentDate,
        Set<CourseSummaryDto> courses
) {}
