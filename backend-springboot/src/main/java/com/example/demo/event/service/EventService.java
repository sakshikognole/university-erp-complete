package com.example.demo.event.service;

import com.example.demo.event.model.Event;
import com.example.demo.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getByEventId(String eventId) {
        return eventRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Event not found with ID: " + eventId));
    }

    public Event create(Event event) {
        if (event.getEventId() == null || event.getEventId().isBlank()) {
            throw new IllegalArgumentException("Event ID is required.");
        }
        if (eventRepository.existsByEventId(event.getEventId().trim())) {
            throw new IllegalArgumentException(
                    "Event ID '" + event.getEventId() + "' already exists.");
        }
        if (event.getStartDate() != null && event.getEndDate() != null
                && event.getEndDate().isBefore(event.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }
        event.setEventId(event.getEventId().trim());
        if (event.getStatus() == null || event.getStatus().isBlank()) {
            event.setStatus("ACTIVE");
        }
        return eventRepository.save(event);
    }

    public Event update(String eventId, Event incoming) {
        Event existing = getByEventId(eventId);

        existing.setEventTitle(incoming.getEventTitle());
        existing.setEventType(incoming.getEventType());
        existing.setOrganizerId(incoming.getOrganizerId());
        existing.setStartDate(incoming.getStartDate());
        existing.setEndDate(incoming.getEndDate());
        existing.setVenueId(incoming.getVenueId());
        existing.setBudget(incoming.getBudget());
        existing.setDescription(incoming.getDescription());
        existing.setStatus(incoming.getStatus());

        if (existing.getStartDate() != null && existing.getEndDate() != null
                && existing.getEndDate().isBefore(existing.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }

        return eventRepository.save(existing);
    }

    public void delete(String eventId) {
        Event existing = getByEventId(eventId);
        eventRepository.delete(existing);
    }
}
