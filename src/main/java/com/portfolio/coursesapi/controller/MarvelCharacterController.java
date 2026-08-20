package com.portfolio.coursesapi.controller;

import com.portfolio.coursesapi.dto.marvel.MarvelResponse;
import com.portfolio.coursesapi.service.MarvelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/marvel")
public class MarvelCharacterController {

    private final MarvelService marvelService;

    // Inyección de dependencias por constructor
    public MarvelCharacterController(MarvelService marvelService) {
        this.marvelService = marvelService;
    }

    @GetMapping("/characters")
    public ResponseEntity<MarvelResponse> getCharacters(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        MarvelResponse response = marvelService.getCharacters(name, limit, offset);
        return ResponseEntity.ok(response);
    }
}
