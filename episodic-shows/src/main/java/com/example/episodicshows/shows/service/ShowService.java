package com.example.episodicshows.shows.service;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.repo.EpisodesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    private final EpisodesRepo episodesRepo;

    @Autowired
    public ShowService(EpisodesRepo episodesRepo) {
        this.episodesRepo = episodesRepo;
    }

    public List<EpisodeEntity> getEpisodesByShowId(Long showId) {
        List<EpisodeEntity> episodes = episodesRepo.findAllByShowId(showId);
        return episodes.stream().map(this::addEpisodeTitle).collect(Collectors.toList());
    }

    public EpisodeEntity createEpisodeOfShow(Long showId, EpisodeEntity episodeRequest) {
        EpisodeEntity savedEpisode = episodesRepo.save(episodeRequest.withShowId(showId));
        return addEpisodeTitle(savedEpisode);
    }

    private EpisodeEntity addEpisodeTitle(EpisodeEntity episode) {
        String episodeTitle = String.format("S%s E%s", episode.getSeasonNumber(), episode.getEpisodeNumber());
        return episode.withTitle(episodeTitle);
    }


}
