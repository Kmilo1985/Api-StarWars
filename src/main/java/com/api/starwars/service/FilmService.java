package com.api.starwars.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.starwars.dto.FilmResponseDTO;
import com.api.starwars.dto.SwapiFilmResponse;
import com.api.starwars.model.FilmEntity;
import com.api.starwars.repository.FilmRepository;

import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmRepository repository;
    private final RestTemplate restTemplate;
    private final String SWAPI_BASE_URL = "https://swapi.dev/api/films/";

    public FilmService(FilmRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Optional<FilmResponseDTO> fetchAndSaveFilm(String id) {
        try {
            if (!id.matches("\\d{1,2}")) {
                throw new IllegalArgumentException("error en la solicitud");
            }
            ResponseEntity<SwapiFilmResponse> response = restTemplate.getForEntity(SWAPI_BASE_URL + id + "/",
                    SwapiFilmResponse.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return Optional.empty();
            }
            SwapiFilmResponse body = response.getBody();

            FilmEntity film = new FilmEntity();
            film.setEpisodeId(body.getEpisode_id());
            film.setTitle(body.getTitle());
            film.setReleaseDate(LocalDate.parse(body.getRelease_date(), DateTimeFormatter.ISO_DATE));

            repository.save(film);

            return Optional.of(new FilmResponseDTO(film.getEpisodeId(), film.getTitle(), film.getReleaseDate()));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<FilmEntity> updateFilm(Long id, FilmEntity update) {
        return repository.findById(id).map(film -> {
            film.setTitle(update.getTitle());
            film.setEpisodeId(update.getEpisodeId());
            film.setReleaseDate(update.getReleaseDate());
            return repository.save(film);
        });
    }

    public boolean deleteFilm(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    protected ResponseEntity<SwapiFilmResponse> getRestTemplateResponse(String url) {
        return restTemplate.getForEntity(url, SwapiFilmResponse.class);
    }
}
