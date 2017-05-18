package com.example.episodicshows.shows.data.repo;

import com.example.episodicshows.shows.data.entity.ShowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowsRepo extends CrudRepository<ShowEntity, Long> {
}
