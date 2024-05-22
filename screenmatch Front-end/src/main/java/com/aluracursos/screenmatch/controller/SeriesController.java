package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.EpisodeDTO;
import com.aluracursos.screenmatch.dto.SeriesDTO;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Path;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService service;

    @GetMapping()
    public List<SeriesDTO> getAllSeries() {
        return service.getAllSeries();
    }
    @GetMapping("/top5")
    public List<SeriesDTO> getTop5(){
        return service.getTop5();
    }
    @GetMapping("/lanzamientos")
    public List<SeriesDTO> getTheMostRecentRelease(){
        return service.getTheMostRecentRelease();
    }
    @GetMapping("/{id}")
    public SeriesDTO getById(@PathVariable Long id){
        return service.getById(id);
    }
    @GetMapping ("/{id}temporadas/todas")
        public List<EpisodeDTO> getAllSeasons(@PathVariable Long id){
        return service.getAllSeasons(id);
    }
    @GetMapping ("/{id}/seasons/{seasonNumber}")
        public List<EpisodeDTO> getSeasonsByNumber(@PathVariable Long id,
                                                   @PathVariable Long seasonNumber){
        return service.getSeasonsByNumber(id, seasonNumber);
    }
    @GetMapping ("/category/{genreName}")
    public List<SeriesDTO> getSeriesByCategory(@PathVariable String genreName){
        return service.getSeasonsByCategory(genreName);
    }

}
