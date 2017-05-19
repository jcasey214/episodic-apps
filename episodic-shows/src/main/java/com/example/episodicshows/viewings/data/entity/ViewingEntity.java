package com.example.episodicshows.viewings.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="viewings")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ViewingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;
    private final Long userId;
    private final Long showId;
    private final Long episodeId;
    @Wither
    private final Date updatedAt;
    @Wither
    private final Integer timecode;
}
