package com.portfolio.coursesapi.controller;

import com.portfolio.coursesapi.dto.country.CountryData;
import com.portfolio.coursesapi.dto.country.CountryResponse;
import com.portfolio.coursesapi.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryApiController {

    private final CountryService countryService;

    // Inyección por constructor (Buena práctica de Spring)
    public CountryApiController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Endpoint GET que consume el servicio funcional
     * @param code Código de dos letras del país (ej: CA, CL, MX)
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<CountryData> getCountryByAlphaCode(@PathVariable String code) {
        return countryService.getCountryByCode(code.toUpperCase())
                .map(ResponseEntity::ok) // Si el Optional tiene valor, retorna 200 OK con el objeto
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si está vacío (404), retorna 404 Not Found
    }
}