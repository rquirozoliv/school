package com.portfolio.coursesapi.dto.generatedata;

import java.time.LocalDate;
import java.util.List;

public record TeacherMockDto(
        Long id,
        String name,
        String email,
        LocalDate enrollmentDate,
        List<Long> courseIds
) {}
