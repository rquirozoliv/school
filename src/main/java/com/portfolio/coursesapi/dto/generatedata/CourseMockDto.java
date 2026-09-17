package com.portfolio.coursesapi.dto.generatedata;

import java.time.LocalDate;
import java.util.List;

public record CourseMockDto(
        Long id, // ID temporal del JSON para resolver las relaciones en memoria
        String code,
        String title,
        Integer credits,
        String description
) {}

