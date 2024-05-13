package com.aluracursos.screenmatch.main;
import com.aluracursos.screenmatch.model.SerieData;
import com.aluracursos.screenmatch.model.SeasonData;

import com.aluracursos.screenmatch.service.ConsumptionAPI;
import com.aluracursos.screenmatch.service.toConvertData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner keyboard = new Scanner(System.in);
    private ConsumptionAPI consumptionAPI = new ConsumptionAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private  final  String API_KEY = "&apikey=d4dbcc70";
    private toConvertData converter = new toConvertData();


    public void showMain() {
        System.out.println("Please write the name of the series you want");
        var seriesName = keyboard.nextLine();

        var json = consumptionAPI.getData(URL_BASE + seriesName.replace(" ", "+") + API_KEY);
        var data = converter.getData(json, SerieData.class);
        System.out.println(data+"\n");

        // search data for all seasons
        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i < data.totalSeasons(); i++) {
            json = consumptionAPI.getData(URL_BASE + "=" + seriesName.replace(" ", "+") + "&Season=" + i + API_KEY);
            var seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }
}
