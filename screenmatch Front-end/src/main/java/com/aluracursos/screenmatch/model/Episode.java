package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

import java.time.DateTimeException;
import java.time.LocalDate;
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating ;
    private LocalDate dateRelease;

    @ManyToOne
    private Series series;

    public Episode(){

    }

    public Episode(Integer number, EpisodeData d) {
        this.season=number;
        this.title=d.title();
        this.episodeNumber =d.numberOfEpisode();
        try{
            this.rating = Double.valueOf(d.rating());
        } catch (NumberFormatException e){
            this.rating=0.0;
        }
        try {
            this.dateRelease = LocalDate.parse(d.dateRelease());
        }catch (DateTimeException e){
            this.dateRelease = null;
        }
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(LocalDate dateRelease) {
        this.dateRelease = dateRelease;
    }

    @Override
    public String toString() {
        return
                "season=" + season +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", rating=" + rating +
                ", dateRelease=" + dateRelease;
    }
}

