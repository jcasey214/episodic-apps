package com.example.episodicshows.viewings.service;

import com.example.episodicshows.listener.QueueMessage;
import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.example.episodicshows.shows.service.ShowService;
import com.example.episodicshows.users.data.entity.UserEntity;
import com.example.episodicshows.users.data.repo.UsersRepo;
import com.example.episodicshows.viewings.data.entity.ViewingEntity;
import com.example.episodicshows.viewings.data.repo.ViewingsRepo;
import com.example.episodicshows.viewings.model.RecentlyWatched;
import com.example.episodicshows.viewings.model.ViewingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ViewingService {
    private final ViewingsRepo viewingsRepo;
    private final ShowService showService;
    private final UsersRepo usersRepo;

    @Autowired
    public ViewingService(ViewingsRepo viewingsRepo, ShowService showService, UsersRepo usersRepo) {
        this.viewingsRepo = viewingsRepo;
        this.showService = showService;
        this.usersRepo = usersRepo;
    }

    public List<RecentlyWatched> getRecentlyWatchedByUser(Long userId) {
        List<ViewingEntity> userViewings = viewingsRepo.findAllByUserIdOrderByUpdatedAt(userId);
        List<Long> showIds = userViewings.stream().map(ViewingEntity::getShowId).collect(Collectors.toList());
        List<Long> episodeIds = userViewings.stream().map(ViewingEntity::getEpisodeId).collect(Collectors.toList());
        Map<Long, ShowEntity> showMap =
                showService.getShowsById(showIds).stream()
                        .collect(Collectors.toMap(ShowEntity::getId, Function.identity()));
        Map<Long, EpisodeEntity> episodeMap =
                showService.getEpisodesById(episodeIds).stream()
                        .collect(Collectors.toMap(EpisodeEntity::getId, Function.identity()));

        return userViewings.stream().map(v -> RecentlyWatched.builder()
                .timecode(v.getTimecode())
                .updatedAt(v.getUpdatedAt())
                .show(showMap.get(v.getShowId()))
                .episode(episodeMap.get(v.getEpisodeId()))
                .build()).collect(Collectors.toList());
    }

    public void patchViewing(Long userId, ViewingRequest viewingRequest) {
        EpisodeEntity episode = showService.findEpisodeById(viewingRequest.getEpisodeId());
        ViewingEntity existingViewing = viewingsRepo.findByShowIdAndUserId(episode.getShowId(), userId);
        ViewingEntity updatedViewing;
        if (existingViewing != null) {
            updatedViewing = existingViewing
                    .withTimecode(viewingRequest.getTimecode())
                    .withUpdatedAt(viewingRequest.getUpdatedAt());
        } else {
            updatedViewing = mapNewViewing(userId, viewingRequest, episode);
        }
        viewingsRepo.save(updatedViewing);
    }

    public void createViewing(QueueMessage message) {
        EpisodeEntity episode = showService.findEpisodeById(message.getEpisodeId());
        if (episode == null) {
            return;
        }
        UserEntity user = usersRepo.findOne(message.getUserId());
        if(user == null) {
            return;
        }
        ViewingEntity existingViewing = viewingsRepo.findByShowIdAndUserId(episode.getShowId(), message.getUserId());
        ViewingEntity updatedViewing;
        if (existingViewing != null) {
            updatedViewing = existingViewing
                    .withTimecode(message.getOffset())
                    .withUpdatedAt(message.getCreatedAt());
        } else {
            updatedViewing = mapNewViewing(message.getUserId(), message, episode);
        }
        viewingsRepo.save(updatedViewing);
    }

    private ViewingEntity mapNewViewing(Long userId, ViewingRequest viewingRequest, EpisodeEntity episode) {
        return ViewingEntity.builder()
                .episodeId(viewingRequest.getEpisodeId())
                .showId(episode.getShowId())
                .timecode(viewingRequest.getTimecode())
                .userId(userId)
                .updatedAt(viewingRequest.getUpdatedAt())
                .build();
    }

    private ViewingEntity mapNewViewing(Long userId, QueueMessage message, EpisodeEntity episode) {
        return ViewingEntity.builder()
                .episodeId(message.getEpisodeId())
                .showId(episode.getShowId())
                .timecode(message.getOffset())
                .userId(userId)
                .updatedAt(message.getCreatedAt())
                .build();
    }
}
