package com.example.demo.event.repository;

import com.example.demo.event.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findByEventId(String eventId);
    boolean existsByEventId(String eventId);
}
