package com.aluracursos.screenmatch.main;
import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumptionAPI;
import com.aluracursos.screenmatch.service.toConvertData;

import java.util.*;
import java.util.stream.Collectors;


public class Main {
    private Scanner keyboard = new Scanner(System.in);
    private ConsumptionAPI consumptionAPI = new ConsumptionAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d4dbcc70";
    private toConvertData converter = new toConvertData();
    private List<SerieData> seriesData = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Series> seriesList;
    private Optional<Series> seriesSearch;
    public Main(SerieRepository repository) {

        this.repositorio = repository;
    }


    public void showMain() {

        var option = -1;
                while (option !=0){
                    var menu = """
                            1 - Search Series.
                            2 - Search Episodes.
                            3 - Show searches Series.
                            4 - Search Series by title.
                            5 - The top 5 series.
                            6 - Search Series by category.
                            7 - Filter Series.
                            8 - Search episode by title
                            9 - Top 5 episodes by series
                            
                            0 - Exit.
                   
                            """;
                    System.out.println(menu);
                    option = keyboard.nextInt();
                    keyboard.nextLine();

                   switch (option){
                       case 1:
                           searchWebSeries();
                           break;
                       case 2:
                           searchEpisodeBySeries();
                           break;
                       case 3:
                           showSearchSeries();
                           break;
                       case 4:
                           searchSeriesByTitle();
                           break;
                       case 5:
                           searchTop5Series();
                           break;
                       case 6:
                           searchSeriesByCategory();
                           break;
                       case 7:
                           filterSeriesBySeasonAndRating();
                           break;
                       case 8 :
                           SearchEpisodesByTitle();
                           break;
                       case 9:
                           top5EpisodesBySeries();
                           break;
                       case 0:
                           System.out.println("Finish program...");
                           break;
                       default:
                           System.out.println("Option not valid");
                   }
                }
    }



    private SerieData getSerieData(){
        System.out.println("Please write the name of the series you want to search");
        var seriesName = keyboard.nextLine();
        var json = consumptionAPI.getData(URL_BASE+ seriesName.replace(" ","+")+API_KEY);
        SerieData data = converter.getData(json, SerieData.class);
        return data;
    }
    private void searchEpisodeBySeries(){
        //SerieData serieData = getSerieData();
        showSearchSeries();
        System.out.println("Write the series name you want to search for episodes");
        var nameSeries = keyboard.nextLine();

        Optional<Series> series = seriesList.stream()
                .filter(s-> s.getTitle().toLowerCase().contains(nameSeries.toLowerCase()))
                .findFirst();

        if(series.isPresent()){
            var foundSeries = series.get();

            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <foundSeries.getTotalSeasons(); i++) {
                var json = consumptionAPI.getData(URL_BASE + foundSeries.getTitle().replace(" ", "+")+"&season="+i+API_KEY);
                SeasonData seasonData =converter.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episode().stream()
                    .map( e -> new Episode( d.number(), e)))
                    .collect(Collectors.toList());
            foundSeries.setEpisodes(episodes);
            repositorio.save(foundSeries);
        }



    }
    private void searchWebSeries(){
        SerieData data= getSerieData();
        //seriesData.add(data); se sustituye por base de datos
        Series series = new Series(data);
        repositorio.save(series);

        System.out.println(data);
    }
    private void showSearchSeries(){
        seriesList = repositorio.findAll();

        seriesList.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }
    private void searchSeriesByTitle(){
        System.out.println("Write the series name you want to search");
        var nameSeries = keyboard.nextLine();
        seriesSearch = repositorio.findByTitleContainsIgnoreCase(nameSeries);
        if (seriesSearch.isPresent()){
            System.out.println("The series searched is:"+ seriesSearch );
        }else{
            System.out.println("Series not found");
        }
    }
    private void searchTop5Series() {
        List<Series> topSeries = repositorio.findTop5ByOrderByRatingDesc();
        topSeries.forEach(series ->
                System.out.println("Serie: "+series.getTitle()+"Rating"+ series.getRating()));
    }
    private void  searchSeriesByCategory(){

        System.out.println("Please enter the genre/category of the series you want");
        var genry = keyboard.nextLine();
        var category = Category.fromString(genry);
        List<Series> seriesByCategory = repositorio.findByGenre(category);
        System.out.println("The series in the category are" +genry);
        seriesByCategory.forEach(System.out::println);

    }
    private void filterSeriesBySeasonAndRating(){
        System.out.println("How many seasons does the series have");
        var totalSeasons = keyboard.nextInt();
        System.out.println("From what rating do you want to search?");
        var rating = keyboard.nextDouble();
        keyboard.nextLine();

        //List<Series> seriesFilter = repositorio.findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(totalSeasons, rating);
        List<Series> seriesFilter = repositorio.seriesPerSeasonAndRating(totalSeasons,rating);
        System.out.println("----------Filtered series--------- ");
        seriesFilter.forEach(series ->
                System.out.println(series.getTitle()+ " - rating: "+ series.getRating()));
    }
    private void SearchEpisodesByTitle(){
        System.out.println("What´s the name of the episode you want to search for");
        var episodesName = keyboard.nextLine();
        List<Episode> episodesFound = repositorio.episodeByName(episodesName);
        episodesFound.forEach(e ->
                System.out.printf("Series: %s Season %s Episode %s Rating %s\n",
                        e.getSeries().getTitle(),e.getSeason(),e.getEpisodeNumber(),e.getRating()));

    }
    private void top5EpisodesBySeries(){
        searchSeriesByTitle();
        if(seriesSearch.isPresent()){
            Series series = seriesSearch.get();
            List<Episode> topEpisodes = repositorio.top5Episodes(series);
            topEpisodes.forEach(e ->
                    System.out.printf("Series: %s - Season %s -  Number %s -Episode name %s - Rating %s\n",
                            e.getSeries().getTitle(),e.getSeason(),e.getEpisodeNumber(),e.getTitle(),e.getRating()));

        }
    }
}