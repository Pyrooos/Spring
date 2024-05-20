package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SeriesDTO;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<SeriesDTO> getTheMostRecentRelease
}
