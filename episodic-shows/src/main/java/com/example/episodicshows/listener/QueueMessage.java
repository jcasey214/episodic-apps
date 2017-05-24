package com.example.episodicshows.listener;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Builder
@Data
public class QueueMessage {
    @JsonProperty
    private final Long userId;
    @JsonProperty
    private final Long episodeId;
    @JsonProperty
    private final Date createdAt;
    @JsonProperty
    private final Integer offset;

    @JsonCreator
    public QueueMessage(
            @JsonProperty("userid") Long userId,
            @JsonProperty("episodeId") Long episodeId,
            @JsonProperty("createdAt") Date createdAt,
            @JsonProperty("offset") Integer offset) {
        this.userId = userId;
        this.episodeId = episodeId;
        this.createdAt = createdAt;
        this.offset = offset;
    }
}
