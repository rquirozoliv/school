package com.portfolio.coursesapi.service.impl;

import com.portfolio.coursesapi.dto.marvel.MarvelResponse;
import com.portfolio.coursesapi.service.MarvelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestClient;

@Service
public class MarvelServiceImpl implements MarvelService {

    private final RestClient restClient;

    @Value("${marvel.api.public-key}")
    private String publicKey;

    @Value("${marvel.api.private-key}")
    private String privateKey;

    public MarvelServiceImpl() {
        // Inicializa el cliente HTTP apuntando a la URL base de Marvel
        this.restClient = RestClient.builder()
                .baseUrl("https://gateway.marvel.com/v1/public")
                .build();
    }

    @Override
    public MarvelResponse getCharacters(String name, int limit, int offset) {
        long timestamp = System.currentTimeMillis();
        String hash = generateMarvelHash(timestamp);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/characters")
                        .queryParam("ts", timestamp)
                        .queryParam("apikey", publicKey)
                        .queryParam("hash", hash)
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .queryParamIfPresent("name", java.util.Optional.ofNullable(name))
                        .build())
                .retrieve()
                .body(MarvelResponse.class); // Mapea automáticamente el JSON a tu objeto Java
    }

    // Genera el Hash MD5 obligatorio: md5(timestamp + privateKey + publicKey)
    private String generateMarvelHash(long timestamp) {
        String valueToHash = timestamp + privateKey + publicKey;
        return DigestUtils.md5DigestAsHex(valueToHash.getBytes());
    }
}