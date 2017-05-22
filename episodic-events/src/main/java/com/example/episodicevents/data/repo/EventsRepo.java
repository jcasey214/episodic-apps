package com.example.episodicevents.data.repo;

import com.example.episodicevents.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends MongoRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);
}
