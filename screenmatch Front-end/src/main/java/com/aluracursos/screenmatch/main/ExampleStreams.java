package com.aluracursos.screenmatch.main;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ExampleStreams {
    public void showExample(){
        List<String> names = Arrays.asList("Gabo","Adith", "Luis","Cristian","Maria", "Zaida");
        names.stream()
                .sorted()
                .filter(n-> n.startsWith("L"))
                .map(n -> n.toUpperCase())
                .limit(4)
                .forEach(System.out::println);


    }
}
