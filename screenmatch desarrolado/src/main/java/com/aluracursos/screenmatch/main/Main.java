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
        Optional<Series> seriesSearch = repositorio.findByTitleContainsIgnoreCase(nameSeries);
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

        List<Series> seriesFilter = repositorio.findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(totalSeasons, rating);
        System.out.println("----------Filtered series--------- ");
        seriesFilter.forEach(series ->
                System.out.println(series.getTitle()+ " - rating: "+ series.getRating()));
    }
}