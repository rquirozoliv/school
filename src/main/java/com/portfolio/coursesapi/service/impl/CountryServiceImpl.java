package com.portfolio.coursesapi.service.impl;

import com.portfolio.coursesapi.dto.country.CountryData;
import com.portfolio.coursesapi.dto.country.CountryResponse;
import com.portfolio.coursesapi.dto.country.DataContainer;
import com.portfolio.coursesapi.dto.country.RestCountriesResponse;
import com.portfolio.coursesapi.restclient.RestCountriesClient;
import com.portfolio.coursesapi.restclient.RestCountriesV5Client;
import com.portfolio.coursesapi.service.CountryService;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final RestCountriesV5Client countriesClient;
    private static final String AUTH_TOKEN = "Bearer rc_live_demo";

    public CountryServiceImpl(RestCountriesV5Client countriesClient) {
        this.countriesClient = countriesClient;
    }

    /**
     * Consume la API v5 de forma funcional y segura
     */
    public Optional<CountryData> getCountryByCode(String code) {
        try {
            return Optional.ofNullable(countriesClient.getCountryByAlpha2(AUTH_TOKEN, code))
                    .map(RestCountriesResponse::data)
                    .map(DataContainer::objects)
                    .flatMap(list -> list.stream().findFirst()); // Extrae el primer elemento funcionalmente
        } catch (FeignException.NotFound e) {
            return Optional.empty(); // 404 manejado limpiamente
        } catch (FeignException e) {
            // Manejo de otros errores HTTP o de red
            return Optional.empty();
        }
    }
}




    /*

    private final RestCountriesClient countriesClient;

    private final RestCountriesV5Client countriesClient;
    private static final String AUTH_TOKEN = "Bearer rc_live_demo";

    // Inyección por constructor (Buena práctica tradicional de Spring)
    public CountryServiceImpl(RestCountriesClient countriesClient) {
        this.countriesClient = countriesClient;
    }

    @Override
    public List<CountryResponse> getCountriesByLanguage(String language) {
        try {
            // Programación funcional: Flujo declarativo para procesar la lista
            return Optional.ofNullable(countriesClient.getAllCountries())
                    .orElse(List.of())
                    .stream()
                    .filter(country -> country.languages() != null &&
                            country.languages().containsValue(language))
                    .toList(); // Expresión inmutable de Java 21
        } catch (FeignException e) {
            // Log de error omitido por simplicidad, retorna lista vacía de forma segura
            return List.of();
        }
    }

    @Override
    public Optional<CountryResponse> getCountryByName(String name) {
        try {
            List<CountryResponse> response = countriesClient.getCountryByName(name);

            // Programación funcional: Transforma la lista en un Optional del primer elemento
            return Optional.ofNullable(response)
                    .flatMap(list -> list.stream().findFirst());
        } catch (FeignException.NotFound e) {
            // Si la API responde 404 (País no encontrado), se maneja limpiamente con Optional.empty()
            return Optional.empty();
        } catch (FeignException e) {
            // Cualquier otro error de red o de la API de REST Countries
            return Optional.empty();
        }
    }*/
