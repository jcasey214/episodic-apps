package com.example.episodicevents.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(force = true)
public class FastForwardRewindEvent extends Event{
    @JsonProperty
    private final FastForwardData data;

    public FastForwardRewindEvent(@JsonProperty("id") String id,
                                  @JsonProperty("type") String type,
                                  @JsonProperty("userId") Long userId,
                                  @JsonProperty("showId") Long showId,
                                  @JsonProperty("episodeId") Long episodeId,
                                  @JsonProperty("createdAt") Date createdAt,
                                  @JsonProperty("data") FastForwardData data) {
        super(id, type, userId, showId, episodeId, createdAt);
        this.data = data;
    }

    public static class FastForwardData {
        private final Integer startOffset;
        private final Integer endOffset;
        private final Float speed;

        @JsonCreator
        public FastForwardData(
                @JsonProperty("startOffset") Integer startOffset,
                @JsonProperty("endOffset") Integer endOffset,
                @JsonProperty("speed") Float speed) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.speed = speed;
        }

        public Integer getStartOffset() {
            return startOffset;
        }

        public Integer getEndOffset() {
            return endOffset;
        }

        public Float getSpeed() {
            return speed;
        }
    }
}
