package com.example.episodicshows.shows.controller;

import com.example.episodicshows.shows.data.entity.ShowEntity;
import com.example.episodicshows.shows.data.repo.ShowsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShowsController {
    private final ShowsRepo showsRepo;

    @Autowired
    public ShowsController(ShowsRepo showsRepo) {
        this.showsRepo = showsRepo;
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

}
