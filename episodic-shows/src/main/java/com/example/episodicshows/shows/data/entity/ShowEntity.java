package com.example.episodicshows.shows.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "shows")
@Getter
@NoArgsConstructor(force = true)
public class ShowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    @JsonCreator
    public ShowEntity(@JsonProperty("name") String name) {
        this.name = name;
    }
}
