package com.api.starwars;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.starwars.model.FilmEntity;
import com.api.starwars.repository.FilmRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void saveFilm_success() {
        FilmEntity film = new FilmEntity();
        film.setEpisodeId(1);
        film.setTitle("A New Hope");
        film.setReleaseDate(LocalDate.of(1977, 5, 25));

        FilmEntity saved = filmRepository.save(film);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("A New Hope");
    }
}
