package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Category;
import com.aluracursos.screenmatch.model.Episode;
import com.aluracursos.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Series, Long> {

   Optional<Series> findByTitleContainsIgnoreCase(String nameSeries);

   List<Series> findTop5ByOrderByRatingDesc();
   List<Series> findByGenre(Category category);
   //List<Series> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(int totalSeasons, double rating);
   @Query("SELECT s FROM Series s WHERE s.totalSeasons <= :totalSeasons AND rating >= :rating")
   List<Series> seriesPerSeasonAndRating(int totalSeasons, Double rating);

   @Query("SELECT e FROM Series s JOIN s.episodes e WHERE e.title ILIKE %:episodesName%")
   List<Episode>episodeByName(String episodesName);

   @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series ORDER BY e.rating DESC LIMIT 5 ")
   List<Episode> top5Episodes(Series series);

   @Query("SELECT s FROM Series s" + "JOIN s.episodes e "+ "GROUP BY s"+"ORDER BY MAX(e.dateRelease) DESC LIMIT 5")
   List<Series> theMostRecentRelease();

}
