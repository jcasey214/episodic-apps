package com.example.episodicevents.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayPauseProgressEvent.class, name = "play"),
        @JsonSubTypes.Type(value = PlayPauseProgressEvent.class, name = "pause"),
        @JsonSubTypes.Type(value = FastForwardRewindEvent.class, name = "fastForward"),
        @JsonSubTypes.Type(value = FastForwardRewindEvent.class, name = "rewind"),
        @JsonSubTypes.Type(value = PlayPauseProgressEvent.class, name = "progress"),
        @JsonSubTypes.Type(value = ScrubEvent.class, name = "scrub")
})
@Builder
@NoArgsConstructor(force = true)
public class Event {
    @Id
    @JsonIgnore
    private final String id;
    private final String type;
    private final Long userId;
    private final Long showId;
    private final Long episodeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final Date createdAt;

    @JsonCreator
    public Event(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("userId") Long userId,
            @JsonProperty("showId") Long showId,
            @JsonProperty("episodeId") Long episodeId,
            @JsonProperty("createdAt") Date createdAt
    ) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.showId = showId;
        this.episodeId = episodeId;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", userId=" + userId +
                ", showId=" + showId +
                ", episodeId=" + episodeId +
                ", createdAt=" + createdAt +
                '}';
    }
}
