package com.api.starwars.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.starwars.dto.FilmResponseDTO;
import com.api.starwars.model.FilmEntity;
import com.api.starwars.service.FilmService;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFilm(@PathVariable String id) {
        try {
            Optional<FilmResponseDTO> dto = filmService.fetchAndSaveFilm(id);
            return dto.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.noContent().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error en la solicitud.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable Long id, @RequestBody FilmEntity updatedFilm) {
        return filmService.updateFilm(id, updatedFilm)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable Long id) {
        return filmService.deleteFilm(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}