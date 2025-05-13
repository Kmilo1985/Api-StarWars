package com.api.starwars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.api.starwars.dto.FilmResponseDTO;
import com.api.starwars.dto.SwapiFilmResponse;
import com.api.starwars.model.FilmEntity;
import com.api.starwars.repository.FilmRepository;
import com.api.starwars.service.FilmService;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;

    @Spy
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAndSaveFilm_validId_returnsDTO() {
        // Arrange
        String id = "1";
        SwapiFilmResponse swapiResponse = new SwapiFilmResponse();
        swapiResponse.setEpisode_id(4);
        swapiResponse.setTitle("A New Hope");
        swapiResponse.setRelease_date("1977-05-25");

        ResponseEntity<SwapiFilmResponse> responseEntity = new ResponseEntity<>(swapiResponse, HttpStatus.OK);

        when(restTemplate.getForEntity("https://swapi.dev/api/films/1/", SwapiFilmResponse.class))
                .thenReturn(responseEntity);

        // Act
        Optional<FilmResponseDTO> result = filmService.fetchAndSaveFilm(id);

        // Assert
        assertTrue(result.isPresent());
        FilmResponseDTO dto = result.get();
        assertEquals(4, dto.getEpisodeId());
        assertEquals("A New Hope", dto.getTitle());
        assertEquals(LocalDate.of(1977, 5, 25), dto.getReleaseDate());

        verify(filmRepository).save(any(FilmEntity.class));
    }

    @Test
    void fetchAndSaveFilm_invalidId_throwsException() {
        String id = "abc";
        assertThrows(IllegalArgumentException.class, () -> filmService.fetchAndSaveFilm(id));
    }

    @Test
    void updateFilm_existingId_updatesEntity() {
        Long id = 1L;
        FilmEntity existing = new FilmEntity();
        existing.setId(id);
        existing.setEpisodeId(4);
        existing.setTitle("A New Hope");
        existing.setReleaseDate(LocalDate.of(1977, 5, 25));

        FilmEntity update = new FilmEntity();
        update.setEpisodeId(5);
        update.setTitle("The Empire Strikes Back");
        update.setReleaseDate(LocalDate.of(1980, 5, 21));

        when(filmRepository.findById(id)).thenReturn(Optional.of(existing));
        when(filmRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Optional<FilmEntity> result = filmService.updateFilm(id, update);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getEpisodeId());
        assertEquals("The Empire Strikes Back", result.get().getTitle());
    }

    @Test
    void updateFilm_notFound_returnsEmpty() {
        when(filmRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<FilmEntity> result = filmService.updateFilm(1L, new FilmEntity());

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteFilm_existingId_deletes() {
        when(filmRepository.existsById(1L)).thenReturn(true);

        boolean result = filmService.deleteFilm(1L);

        assertTrue(result);
        verify(filmRepository).deleteById(1L);
    }

    @Test
    void deleteFilm_nonExistingId_returnsFalse() {
        when(filmRepository.existsById(99L)).thenReturn(false);

        boolean result = filmService.deleteFilm(99L);

        assertFalse(result);
    }
}
