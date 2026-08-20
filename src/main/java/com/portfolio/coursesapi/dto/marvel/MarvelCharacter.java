package com.portfolio.coursesapi.dto.marvel;

public record MarvelCharacter(
        Long id,
        String name,
        String description,
        MarvelThumbnail thumbnail
) {}