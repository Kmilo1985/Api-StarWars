package com.api.starwars.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.api.starwars.dto.FilmResponseDTO;
import com.api.starwars.dto.SwapiFilmResponse;

import org.springframework.web.client.HttpClientErrorException;

@Component
public class SwapiClient {
    private final RestTemplate restTemplate;
    private static final String SWAPI_URL = "https://swapi.dev/api/films/%d/";

    public SwapiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SwapiFilmResponse getFilmById(Long id) {
        try {
            return restTemplate.getForObject(String.format(SWAPI_URL, id), SwapiFilmResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null; // Retorna null si no existe
        }
    }
}
