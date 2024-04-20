package com.felipe.screenmatch.main;

import com.felipe.screenmatch.models.Episode;
import com.felipe.screenmatch.models.Season;
import com.felipe.screenmatch.models.TvShow;
import com.felipe.screenmatch.service.ConsumeAPI;
import com.felipe.screenmatch.service.DataConversion;

import javax.swing.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private final Scanner reader = new Scanner(System.in);
    private final ConsumeAPI consume = new ConsumeAPI();
    private final DataConversion convert = new DataConversion();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public static void main(String[] args) {

    }

    public void showMenu() {
        System.out.println("Type the Tv Show name that you wish to look for: ");
        var tvShowName = reader.nextLine();

        String showURI = URLEncoder.encode(tvShowName, StandardCharsets.UTF_8);
        String finalAddress = ADDRESS + showURI + API_KEY;

        String json = consume.getData(finalAddress);

        TvShow showData = convert.getData(json, TvShow.class);

        List<Season> seasons = new ArrayList<>();

        for (int i = 1; i <= showData.totalSeasons(); i += 1 ) {
            String localJson = consume.getData(ADDRESS + showURI + "&season=" + i + API_KEY);
            Season seasonData = convert.getData(localJson, Season.class);

            seasons.add(seasonData);
        }

        // Two different ways of using lambda, similar to JS arrow functions
        // If you want to pass the type or more than 1 parameter needs the ()
        // seasons.forEach((season) -> season.episodes().forEach(e -> System.out.println(e.title())));
        // The next line prints the whole information about the episode
        // seasons.forEach((season) -> season.episodes().forEach(System.out::println));
        // (parameter) -> { expression }

//        List<EpisodeData> episodesData;
//        episodesData = seasons.stream()
//                .flatMap(t -> t.episodes().stream())
//                .collect(Collectors.toList());
//
//        episodesData.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        List<Episode> episodes;
        episodes = seasons.stream()
                .flatMap(t ->
                        t.episodes().stream()
                        .map(e -> new Episode(t.number(), e)))
                .toList();
        // .toList() creates a immutable list
        // .collect(Collectors(collections.toList()) creates a mutable list

//        episodes.stream()
//                .sorted(Comparator.comparing(Episode::getRating).reversed())
//                .limit(5)
//                .forEach(System.out::println);
//
//        episodes.forEach(System.out::println);
        List<Integer> years;
        years = episodes.stream()
                .filter(e -> e.getReleaseDate() != null)
                .map(e -> e.getReleaseDate().getYear())
                .collect(Collectors.toSet())
                .stream()
                .sorted()
                .toList();


        System.out.println("Select a year for " + tvShowName + ":");
        int counter = 1;
        for (int year : years) {
            System.out.println(counter + ". " + year);
            counter++;
        }
        System.out.println("Enter your selection (or 0 to quit): ");
        int year = reader.nextInt();
        reader.nextLine();

        LocalDate searchYear = LocalDate.of(year, 1, 1);
        System.out.printf("%nTop 10 %s episodes from %d onwards: %n", tvShowName,year);
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(e ->
                        e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchYear))
                .sorted(Comparator.comparing(Episode::getRating).reversed())
                .limit(10)
                .forEach(e -> System.out.println(
                        "Season: " + e.getSeason() +
                        " - Episode: " + e.getTitle() +
                        " - Release Year: " + e.getReleaseDate().format(formatDate) +
                        " - Rating: " + e.getRating()
                ));

    }
}


// Example on how to work with stream() and lambdas
// List<String> names = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");

// names.stream()
//         .sorted()
//         .limit(3)
//         .filter(n -> n.startsWith("N"))
//         .map(String::toUpperCase)
//         .forEach(System.out::println);