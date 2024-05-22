package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodeDTO;
import com.aluracursos.screenmatch.dto.SeriesDTO;
import com.aluracursos.screenmatch.model.Category;
import com.aluracursos.screenmatch.model.Episode;
import com.aluracursos.screenmatch.model.Series;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {


    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SeriesDTO> getAllSeries() {
        return dataConvert(repository.findAll());
    }
    public List<SeriesDTO> getTop5(){
        return dataConvert(repository.findTop5ByOrderByRatingDesc());
    }

    public List<SeriesDTO> getTheMostRecentRelease(){
        return dataConvert(repository.theMostRecentRelease());
    }
    public List<SeriesDTO> dataConvert(List<Series>series){
       return series.stream()
                .map(s -> new SeriesDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getPoster(),
                        s.getGenre(), s.getActors(), s.getPlot()))
                .collect(Collectors.toList());
    }

    public SeriesDTO getById(Long id) {
        Optional<Series> series = repository.findById(id);
        if(series.isPresent()){
            Series s = series.get();
            return new SeriesDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getPoster(),
                    s.getGenre(), s.getActors(), s.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Series> series = repository.findById(id);
        if(series.isPresent()){
            Series s = series.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getSeason(), e.getTitle(), e.getEpisodeNumber()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDTO> getSeasonsByNumber(Long id, Long seasonNumber) {
        return repository.getSeasonsByNumber(id, seasonNumber).stream()
                .map( e -> new EpisodeDTO(e.getSeason(), e.getTitle(), e.getEpisodeNumber()))
                .collect(Collectors.toList());
    }

    public List<SeriesDTO> getSeasonsByCategory(String genreName) {
        Category category = Category.fromString(genreName);
        return dataConvert(repository.findByGenre(category));
    }
}
