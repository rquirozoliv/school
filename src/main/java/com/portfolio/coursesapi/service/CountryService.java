package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.dto.country.CountryData;
import com.portfolio.coursesapi.dto.country.CountryResponse;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    Optional<CountryData> getCountryByCode(String code);
}
