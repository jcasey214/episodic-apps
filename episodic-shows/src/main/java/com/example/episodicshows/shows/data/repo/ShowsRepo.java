package com.example.episodicshows.shows.data.repo;

import com.example.episodicshows.shows.data.entity.ShowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowsRepo extends CrudRepository<ShowEntity, Long> {
    List<ShowEntity> findAllByIdIn(List<Long> showIds);
}
