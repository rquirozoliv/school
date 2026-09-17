package com.portfolio.coursesapi.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record TeacherResponseDto(
        Long id,
        String rut,
        String name,
        String email,
        LocalDate enrollmentDate,
        Set<CourseSummaryDto> courses
) {}
