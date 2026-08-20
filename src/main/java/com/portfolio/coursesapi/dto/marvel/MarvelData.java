package com.portfolio.coursesapi.dto.marvel;

import java.util.List;

public record MarvelData(
        int offset,
        int limit,
        int total,
        List<MarvelCharacter> results
) {}
