package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.model.Episode;
import com.aluracursos.screenmatch.model.EpisodeData;
import com.aluracursos.screenmatch.model.SerieData;
import com.aluracursos.screenmatch.model.SeasonData;

import com.aluracursos.screenmatch.service.ConsumptionAPI;
import com.aluracursos.screenmatch.service.toConvertData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner keyboard = new Scanner(System.in);
    private ConsumptionAPI consumptionAPI = new ConsumptionAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d4dbcc70";
    private toConvertData converter = new toConvertData();


    public void showMain() {
        System.out.println("Please write the name of the series you want");
        var seriesName = keyboard.nextLine();

        var json = consumptionAPI.getData(URL_BASE + seriesName.replace(" ", "+") + API_KEY);
        var data = converter.getData(json, SerieData.class);
        System.out.println(data + "\n");

        // search data for all seasons
        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i < data.totalSeasons(); i++) {
            json = consumptionAPI.getData(URL_BASE + "=" + seriesName.replace(" ", "+") + "&Season=" + i + API_KEY);
            var seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        //seasons.forEach(System.out::println);

        // SHOW ONLY the title of the episodes in a seasons

        /*for (int i = 0; i < data.totalSeasons() ; i++) {
            List<EpisodeData> seasonEpisodes = seasons.get(i).episode();
            for (int j = 0; j < seasonEpisodes.size() ; j++) {
                System.out.println(seasonEpisodes.get(j).title());

            }
        }*/
        //this is another way to do a for loop
        //seasons.forEach(t -> t.episode().forEach(e -> System.out.println(e.title())));
        // Convert all info to a list of EpisodesData

        List<EpisodeData> episodeData = seasons.stream()
                .flatMap(t -> t.episode().stream())
                .collect(Collectors.toList());

        //Top 5 episodes
        /*System.out.println("Top 5 episodes:");
        episodeData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("First filter (N/A)" + e))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .peek(e -> System.out.println("second ordered (M>m)" + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("Third filter mayus (m/M) " + e))
                .limit(5)
                .forEach(System.out::println);*/

        //converting data to an episode list
        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episode().stream()
                        .map(d -> new Episode(t.number(), d)))
                .collect(Collectors.toList());
        //episodes.forEach(System.out::println);

        //search episodes starting from the year
        /*System.out.println("Please write the year of the series you want ");
        var date = keyboard.nextInt();
        keyboard.nextLine();*/

       /* LocalDate dateSearch = LocalDate.of(date, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
*/
//        episodes.stream()
//                .filter(e ->e.getDateRelease() != null && e.getDateRelease().isAfter(dateSearch))
//                .forEach(e -> System.out.println(
//                        "Season " + e.getSeason()+
//
//                                " Episode: "+e.getTitle()+
//                                " Release date: "+ e.getDateRelease().format(dtf)
//                ));
        // search a part of the title on an episode
        /*System.out.println("Please write a part of the title you want search");
         var partOfTitle =keyboard.nextLine();
        Optional<Episode> episodeSearch = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(partOfTitle.toUpperCase()))
                .findFirst();
        if (episodeSearch.isPresent()){
            System.out.println("Episode found");
            System.out.println("The data is: "+ episodeSearch.get());
        } else {
            System.out.println("Episode not found");
        }*/
        Map<Integer, Double> ratingsPerSeason = episodes.stream()
                .filter(e-> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
        System.out.println(ratingsPerSeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e-> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println(est);
        System.out.println("Media of the ratings:"+est.getAverage());
        System.out.println("The Best episode rated:"+ est.getMax());
        System.out.println("The worst episode rated"+est.getMin());
    }
}