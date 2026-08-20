package com.portfolio.coursesapi.dto.marvel;

public record MarvelResponse(
        int code,
        String status,
        MarvelData data
) {}
