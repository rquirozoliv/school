package com.portfolio.coursesapi.dto.generatedata;

import java.time.LocalDate;
import java.util.List;

public record StudentMockDto(
        Long id,
        String name,
        String email,
        Integer age,
        LocalDate enrollmentDate,
        List<Long> courseIds
) {}
