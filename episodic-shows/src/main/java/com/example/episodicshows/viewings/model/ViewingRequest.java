package com.example.episodicshows.viewings.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Date;

@Value
public class ViewingRequest {
    private final Long episodeId;
    private final Date updatedAt;
    private final Integer timecode;

    @JsonCreator
    public ViewingRequest(
            @JsonProperty("episodeId") Long episodeId,
            @JsonProperty("updatedAt") Date updatedAt,
            @JsonProperty("timecode") Integer timecode
    ) {
        this.episodeId = episodeId;
        this.updatedAt = updatedAt;
        this.timecode = timecode;
    }
}