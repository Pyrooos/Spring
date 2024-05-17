package com.aluracursos.desafio.main;

import com.aluracursos.desafio.model.Data;
import com.aluracursos.desafio.model.DataBook;
import com.aluracursos.desafio.service.APIConsumption;
import com.aluracursos.desafio.service.toDataConvert;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private APIConsumption apiConsumption = new APIConsumption();
    private toDataConvert converter =  new toDataConvert();
    private Scanner keyboard = new Scanner(System.in);
    public void showMenu(){
        var json = apiConsumption.getData(URL_BASE);
        System.out.println(json);
        var data = converter.getData(json, Data.class);
        System.out.println(data);

        //top 10: The most downloaded books
        System.out.println("Top 10: The most downloaded books");
        data.resultsBooks().stream()
                .sorted(Comparator.comparing(DataBook::downloadNumber).reversed())
                .limit(10)
                .map(l -> l.title().toUpperCase())
                .forEach(System.out::println);

        // Book searches by name
        System.out.println("Please write the name of the book you want to search:");
        var titleBook = keyboard.nextLine();
        json = apiConsumption.getData(URL_BASE+ "?search="+titleBook.replace(" ","+"));
        var searchData = converter.getData(json, Data.class);
        Optional<DataBook> searchedBook = searchData.resultsBooks().stream()
                .filter(l->l.title().toUpperCase().contains(titleBook.toUpperCase()))
                .findFirst();
        if (searchedBook.isPresent()){
            System.out.println("Found book");
            System.out.println(searchedBook.get());
        } else {
            System.out.println("Book not found");
        }
        DoubleSummaryStatistics est = data.resultsBooks().stream()
                .filter(d->d.downloadNumber() >0 )
                .collect(Collectors.summarizingDouble(DataBook::downloadNumber));
        System.out.println("Media Downloads: "+ est.getAverage());
        System.out.println("Total Downloads: "+ est.getMax());
        System.out.println("Minimum Downloads: "+ est.getMin());



    }
}
