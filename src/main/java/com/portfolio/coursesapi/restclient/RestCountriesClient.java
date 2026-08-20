package com.portfolio.coursesapi.restclient;

import com.portfolio.coursesapi.dto.country.CountryDto;
import com.portfolio.coursesapi.dto.country.CountryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "rest-countries-client", url = "https://restcountries.com")
public interface RestCountriesClient {

    @GetMapping("/all")
    List<CountryResponse> getAllCountries();

    @GetMapping("/name/{name}")
    List<CountryResponse> getCountryByName(@PathVariable("name") String name);
}