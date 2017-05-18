package com.example.episodicshows.shows.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "episodes")
@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class EpisodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    @JsonInclude(Include.NON_NULL)
    private Long id;
    @JsonProperty("showId")
    @Wither
    private Long showId;
    @JsonProperty("episodeNumber")
    private Integer episodeNumber;
    @JsonProperty("seasonNumber")
    private Integer seasonNumber;
    @Transient
    @JsonProperty("title")
    @Wither
    private String title;
}
