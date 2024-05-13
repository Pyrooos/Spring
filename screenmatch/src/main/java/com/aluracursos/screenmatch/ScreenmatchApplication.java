package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.model.SeasonData;
import com.aluracursos.screenmatch.model.SerieData;
import com.aluracursos.screenmatch.model.EpisodeData;
import com.aluracursos.screenmatch.service.ConsumptionAPI;
import com.aluracursos.screenmatch.service.toConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumptionAPI = new ConsumptionAPI();
		var json = consumptionAPI.getData("http://www.omdbapi.com/?t=big+bang+theory&apikey=d4dbcc70");
		System.out.println(json);

		toConvertData converter = new toConvertData();
		var data = converter.getData(json, SerieData.class);
		System.out.println(data);
		json = consumptionAPI.getData("http://www.omdbapi.com/?t=big+bang+theory&Season=1&episode=1&apikey=d4dbcc70");
		EpisodeData episode = converter.getData(json, EpisodeData.class);
		System.out.println(episode);

		List<SeasonData> seasons = new ArrayList<>();
		for (int i = 1; i < data.totalSeasons(); i++) {
			json = consumptionAPI.getData("http://www.omdbapi.com/?t=big+bang+theory&Season="+i+"&apikey=d4dbcc70");
			var seasonData = converter.getData(json, SeasonData.class);
			seasons.add(seasonData);
		}seasons.forEach(System.out::println);

	}
}
