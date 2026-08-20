package com.portfolio.coursesapi.restclient;

import com.portfolio.coursesapi.dto.country.RestCountriesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "rest-countries-v5",
        url = "https://api.restcountries.com/countries/v5")
public interface RestCountriesV5Client {

    @GetMapping("/codes.alpha_2/{code}?pretty=1")
    RestCountriesResponse getCountryByAlpha2(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("code") String code
    );
}
