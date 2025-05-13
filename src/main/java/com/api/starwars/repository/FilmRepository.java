package com.api.starwars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.starwars.model.FilmEntity;

public interface FilmRepository extends JpaRepository<FilmEntity, Long> {
}