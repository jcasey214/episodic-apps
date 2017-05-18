package com.example.episodicshows.shows.controller;

import com.example.episodicshows.shows.data.entity.EpisodeEntity;
import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.example.episodicshows.shows.data.repo.ShowsRepo;
import com.example.episodicshows.shows.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShowsController {
    private final ShowsRepo showsRepo;
    private final ShowService showService;

    @Autowired
    public ShowsController(ShowsRepo showsRepo, ShowService showService) {
        this.showsRepo = showsRepo;
        this.showService = showService;
    }

    @GetMapping("/shows")
    public Iterable<ShowEntity> fetchAllShows() {
        return showsRepo.findAll();
    }

    @PostMapping("/shows")
    @ResponseStatus(HttpStatus.CREATED)
    public ShowEntity createShow(@RequestBody ShowEntity show) {
        return showsRepo.save(show);
    }

    @GetMapping("/shows/{showId}/episodes")
    public List<EpisodeEntity> fetchEpisodesOfShow(@PathVariable("showId") Long showId) {
        return showService.getEpisodesByShowId(showId);
    }

    @PostMapping(value = "/shows/{showId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public EpisodeEntity createEpisodeOfShow(
            @PathVariable("showId") Long showId,
            @RequestBody EpisodeEntity episode
    ) {
        return showService.createEpisodeOfShow(showId, episode);
    }

}
