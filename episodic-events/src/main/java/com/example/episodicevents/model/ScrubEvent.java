package com.example.episodicevents.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(force = true)
public class ScrubEvent extends Event {
    @JsonProperty
    private final ScrubData data;

    public ScrubEvent(@JsonProperty("id") String id,
                      @JsonProperty("type") String type,
                      @JsonProperty("userId") Long userId,
                      @JsonProperty("showId") Long showId,
                      @JsonProperty("episodeId") Long episodeId,
                      @JsonProperty("createdAt") Date createdAt,
                      @JsonProperty("data") ScrubData data) {
        super(id, type, userId, showId, episodeId, createdAt);
        this.data = data;
    }

    public static class ScrubData {
        private final Integer startOffset;
        private final Integer endOffset;

        @JsonCreator
        public ScrubData(
                @JsonProperty("startOffset") Integer startOffset,
                @JsonProperty("endOffset") Integer endOffset) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        public Integer getStartOffset() {
            return startOffset;
        }

        public Integer getEndOffset() {
            return endOffset;
        }
    }
}

