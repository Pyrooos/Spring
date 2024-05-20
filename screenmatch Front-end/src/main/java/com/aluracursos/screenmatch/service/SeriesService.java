package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.SeriesDTO;
import com.aluracursos.screenmatch.model.Series;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
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
                .map(s -> new SeriesDTO(s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getPoster(),
                        s.getGenre(), s.getActors(), s.getPlot()))
                .collect(Collectors.toList());
    }

}
