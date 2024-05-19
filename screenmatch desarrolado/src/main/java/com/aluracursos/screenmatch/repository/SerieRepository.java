package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Category;
import com.aluracursos.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Series, Long> {

   Optional<Series> findByTitleContainsIgnoreCase(String nameSeries);

   List<Series> findTop5ByOrderByRatingDesc();
   List<Series> findByGenre(Category categoryt);
   List<Series> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(int totalTemporadas, Double rating);
}
