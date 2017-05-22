package com.example.episodicevents.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(force = true)
public class PlayPauseProgressEvent extends Event {
    @JsonProperty
    private final PlayData data;

    public PlayPauseProgressEvent(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("userId") Long userId,
            @JsonProperty("showId") Long showId,
            @JsonProperty("episodeId") Long episodeId,
            @JsonProperty("createdAt") Date createdAt,
            @JsonProperty("data") PlayData data) {
        super(id, type, userId, showId, episodeId, createdAt);
        this.data = data;
    }

    public static class PlayData {
        @JsonProperty
        private final Integer offset;

        @JsonCreator
        public PlayData(@JsonProperty("offset") Integer offset) {
            this.offset = offset;
        }

        public Integer getOffset() {
            return offset;
        }
    }
}
