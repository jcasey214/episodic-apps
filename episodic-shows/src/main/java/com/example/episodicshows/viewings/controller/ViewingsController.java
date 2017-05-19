package com.example.episodicshows.viewings.controller;

import com.example.episodicshows.viewings.model.RecentlyWatched;
import com.example.episodicshows.viewings.model.ViewingRequest;
import com.example.episodicshows.viewings.service.ViewingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ViewingsController {
    private final ViewingService viewingService;

    @Autowired
    public ViewingsController(ViewingService viewingService) {
        this.viewingService = viewingService;
    }

    @RequestMapping("/users/{userId}/recently-watched")
    public List<RecentlyWatched> fetchRecentlyWatched(@PathVariable("userId") Long userId) {
        return viewingService.getRecentlyWatchedByUser(userId);
    }

    @PatchMapping("/users/{userId}/viewings")
    public void updateViewing(@PathVariable("userId") Long userId, @RequestBody ViewingRequest viewingRequest) {
        viewingService.patchViewing(userId, viewingRequest);
    }
}
