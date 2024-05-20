package com.aluracursos.screenmatch.dto;

import com.aluracursos.screenmatch.model.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SeriesDTO(
        String title,
        Integer totalSeasons,
        Double rating,
        String poster,
        Category genre,
        String actors,
        String plot) {
}
