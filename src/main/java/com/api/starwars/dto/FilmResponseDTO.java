package com.api.starwars.dto;

import java.time.LocalDate;

public class FilmResponseDTO {
    private int episodeId;
    private String title;
    private LocalDate releaseDate;

    public FilmResponseDTO() {
    }

    public FilmResponseDTO(int episodeId, String title, LocalDate releaseDate) {
        this.episodeId = episodeId;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
