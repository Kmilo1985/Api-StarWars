package com.api.starwars;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import com.api.starwars.controller.FilmController;
import com.api.starwars.dto.FilmResponseDTO;
import com.api.starwars.model.FilmEntity;
import com.api.starwars.service.FilmService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFilm_validId_returnsOk() {
        String id = "1";
        FilmResponseDTO dto = new FilmResponseDTO(1, "A New Hope", LocalDate.of(1977, 5, 25));
        when(filmService.fetchAndSaveFilm(id)).thenReturn(Optional.of(dto));

        ResponseEntity<?> response = controller.getFilm(id);
        assertEquals(200, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getFilm_invalidId_returnsBadRequest() {
        String id = "abc";
        when(filmService.fetchAndSaveFilm(id)).thenThrow(new IllegalArgumentException());

        ResponseEntity<?> response = controller.getFilm(id);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void updateFilm_validRequest_returnsOk() {
        Long id = 1L;
        FilmEntity entity = new FilmEntity();
        when(filmService.updateFilm(eq(id), any())).thenReturn(Optional.of(entity));

        ResponseEntity<?> response = controller.updateFilm(id, entity);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void updateFilm_notFound_returns404() {
        when(filmService.updateFilm(anyLong(), any())).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.updateFilm(999L, new FilmEntity());
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void deleteFilm_exists_returnsNoContent() {
        when(filmService.deleteFilm(1L)).thenReturn(true);

        ResponseEntity<?> response = controller.deleteFilm(1L);
        assertEquals(204, response.getStatusCode());
    }

    @Test
    void deleteFilm_notExists_returnsNotFound() {
        when(filmService.deleteFilm(2L)).thenReturn(false);

        ResponseEntity<?> response = controller.deleteFilm(2L);
        assertEquals(404, response.getStatusCode());
    }
}
