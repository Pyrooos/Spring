package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(

        @JsonAlias("Title") String title,
        @JsonAlias("Episode") Integer numberOfEpisode,
        @JsonAlias("imdbRating") String rating,
        @JsonAlias("Released") String dateRelease
) {
}
