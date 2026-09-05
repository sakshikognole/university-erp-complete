package com.example.demo.event.controller;

import com.example.demo.event.model.Event;
import com.example.demo.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // GET /api/events
    @GetMapping
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    // GET /api/events/{eventId}
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getByEventId(@PathVariable String eventId) {
        try {
            return ResponseEntity.ok(eventService.getByEventId(eventId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/events
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Event event) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(eventService.create(event));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/events/{eventId}
    @PutMapping("/{eventId}")
    public ResponseEntity<?> update(
            @PathVariable String eventId,
            @Valid @RequestBody Event event) {
        try {
            return ResponseEntity.ok(eventService.update(eventId, event));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/events/{eventId}
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> delete(@PathVariable String eventId) {
        try {
            eventService.delete(eventId);
            return ResponseEntity.ok(Map.of("message", "Event deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
