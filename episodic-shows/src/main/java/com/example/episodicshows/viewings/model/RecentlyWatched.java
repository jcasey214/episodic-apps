package com.example.episodicshows.viewings.model;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Builder
@AllArgsConstructor
public class RecentlyWatched {
    @JsonProperty
    private final ShowEntity show;
    @JsonProperty
    private final EpisodeEntity episode;
    @JsonProperty
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final Date updatedAt;
    @JsonProperty
    private final Integer timecode;
}