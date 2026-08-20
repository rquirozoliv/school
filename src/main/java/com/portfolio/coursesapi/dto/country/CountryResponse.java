package com.portfolio.coursesapi.dto.country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryResponse(
        Name name,
        long population,
        Map<String, String> languages,
        Flags flags
) {}

record Name(String common, String official, Map<String, NativeName> nativeName) {}
record NativeName(String official, String common) {}
record Flags(String png, String svg, String alt) {}

record Names(String common, String official) {}

