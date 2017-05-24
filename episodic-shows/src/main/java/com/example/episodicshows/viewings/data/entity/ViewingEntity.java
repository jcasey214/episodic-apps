package com.example.episodicshows.viewings.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "viewings")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewingEntity that = (ViewingEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (showId != null ? !showId.equals(that.showId) : that.showId != null) return false;
        if (episodeId != null ? !episodeId.equals(that.episodeId) : that.episodeId != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
        return timecode != null ? timecode.equals(that.timecode) : that.timecode == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (showId != null ? showId.hashCode() : 0);
        result = 31 * result + (episodeId != null ? episodeId.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (timecode != null ? timecode.hashCode() : 0);
        return result;
    }
}
