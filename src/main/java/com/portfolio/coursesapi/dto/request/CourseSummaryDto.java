package com.portfolio.coursesapi.dto.request;

import java.time.LocalDate;
import java.util.Set;

public record CourseSummaryDto(
        Long id,
        String code,
        String title,
        Integer credits,
        String description
) {}

