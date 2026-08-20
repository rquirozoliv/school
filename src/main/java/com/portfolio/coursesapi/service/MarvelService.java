package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.dto.marvel.MarvelResponse;

public interface MarvelService {
    MarvelResponse getCharacters(String name, int limit, int offset);
}
