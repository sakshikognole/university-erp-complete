package com.example.demo.sport.controller;

import com.example.demo.sport.model.Sport;
import com.example.demo.sport.service.SportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    // GET /api/sports
    @GetMapping
    public ResponseEntity<List<Sport>> getAll() {
        return ResponseEntity.ok(sportService.getAll());
    }

    // GET /api/sports/{sportId}
    @GetMapping("/{sportId}")
    public ResponseEntity<?> getOne(@PathVariable String sportId) {
        try {
            return ResponseEntity.ok(sportService.getBySportId(sportId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/sports
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Sport sport) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(sportService.create(sport));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/sports/{sportId}
    @PutMapping("/{sportId}")
    public ResponseEntity<?> update(
            @PathVariable String sportId,
            @Valid @RequestBody Sport sport) {
        try {
            return ResponseEntity.ok(sportService.update(sportId, sport));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/sports/{sportId}
    @DeleteMapping("/{sportId}")
    public ResponseEntity<?> delete(@PathVariable String sportId) {
        try {
            sportService.delete(sportId);
            return ResponseEntity.ok(Map.of("message", "Sport deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
