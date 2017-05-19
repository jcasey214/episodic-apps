package com.example.episodicshows.shows.service;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.example.episodicshows.shows.data.repo.EpisodesRepo;
import com.example.episodicshows.shows.data.repo.ShowsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    private final EpisodesRepo episodesRepo;
    private final ShowsRepo showsRepo;

    @Autowired
    public ShowService(EpisodesRepo episodesRepo, ShowsRepo showsRepo) {
        this.episodesRepo = episodesRepo;
        this.showsRepo = showsRepo;
    }

    public List<EpisodeEntity> getEpisodesByShowId(Long showId) {
        List<EpisodeEntity> episodes = episodesRepo.findAllByShowId(showId);
        return episodes.stream().map(this::addEpisodeTitle).collect(Collectors.toList());
    }

    public EpisodeEntity createEpisodeOfShow(Long showId, EpisodeEntity episodeRequest) {
        EpisodeEntity savedEpisode = episodesRepo.save(episodeRequest.withShowId(showId));
        return addEpisodeTitle(savedEpisode);
    }

    public List<ShowEntity> getShowsById(List<Long> showIds) {
        return showsRepo.findAllByIdIn(showIds);
    }

    public List<EpisodeEntity> getEpisodesById(List<Long> episodeIds) {
        return episodesRepo.findAllByIdIn(episodeIds).stream().map(this::addEpisodeTitle).collect(Collectors.toList());
    }

    private EpisodeEntity addEpisodeTitle(EpisodeEntity episode) {
        String episodeTitle = String.format("S%s E%s", episode.getSeasonNumber(), episode.getEpisodeNumber());
        return episode.withTitle(episodeTitle);
    }

    public EpisodeEntity findEpisodeById(Long episodeId) {
        return episodesRepo.findById(episodeId);
    }
}
