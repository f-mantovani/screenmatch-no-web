package com.felipe.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvShow(@JsonAlias("Title") String title,
                     @JsonAlias("totalSeasons") int totalSeasons,
                     @JsonAlias("imdbRating") String rating) {

}
