package com.portfolio.coursesapi.dto.country;

import java.util.List;

public record CountryDto(
        Name name,
        List<String> capital,
        long population,
        String region
) {
    public record Name(String common, String official) {}
}