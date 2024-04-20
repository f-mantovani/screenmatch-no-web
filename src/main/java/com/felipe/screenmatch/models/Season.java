package com.felipe.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Season(
        @JsonAlias("Season") int number,
        @JsonAlias("Episodes")List<EpisodeData> episodes
) {}
