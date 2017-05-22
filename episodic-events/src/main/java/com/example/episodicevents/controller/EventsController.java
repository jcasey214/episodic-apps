package com.example.episodicevents.controller;

import com.example.episodicevents.data.repo.EventsRepo;
import com.example.episodicevents.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventsController {

    private final EventsRepo eventsRepo;

    @Autowired
    public EventsController(EventsRepo eventsRepo) {
        this.eventsRepo = eventsRepo;
    }

    @PostMapping("/")
    public void createEvent(@RequestBody Event event) throws JsonProcessingException {
        eventsRepo.save(event);
    }

    @GetMapping("/recent")
    public Page<Event> getRecentEvents(@PageableDefault(size = 20, page = 0) Pageable pageable) {
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.DESC, "createdAt"));
        return eventsRepo.findAll(request);
    }
}
