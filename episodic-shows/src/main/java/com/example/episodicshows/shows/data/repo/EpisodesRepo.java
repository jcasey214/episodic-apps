package com.example.episodicshows.shows.data.repo;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodesRepo extends CrudRepository<EpisodeEntity, Long> {
    List<EpisodeEntity> findAllByShowId(Long showId);
    List<EpisodeEntity> findAll();
    List<EpisodeEntity> findAllByIdIn(List<Long> showIds);
    EpisodeEntity findById(Long showIds);
}
