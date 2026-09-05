package com.example.demo.sport.controller;

import com.example.demo.sport.model.SportTeam;
import com.example.demo.sport.service.SportTeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("legacySportTeamController")
@RequestMapping("/api/sport-teams-legacy")
public class SportTeamController {

    private final SportTeamService service;

    public SportTeamController(SportTeamService service) {
        this.service = service;
    }

    // GET /api/sport-teams
    @GetMapping
    public ResponseEntity<List<SportTeam>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET /api/sport-teams/by-sport/{sportId}
    @GetMapping("/by-sport/{sportId}")
    public ResponseEntity<List<SportTeam>> getBySport(@PathVariable String sportId) {
        return ResponseEntity.ok(service.getBySportId(sportId));
    }

    // GET /api/sport-teams/{teamId}
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getOne(@PathVariable String teamId) {
        try {
            return ResponseEntity.ok(service.getByTeamId(teamId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/sport-teams
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SportTeam team) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(team));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/sport-teams/{teamId}
    @PutMapping("/{teamId}")
    public ResponseEntity<?> update(
            @PathVariable String teamId,
            @Valid @RequestBody SportTeam team) {
        try {
            return ResponseEntity.ok(service.update(teamId, team));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/sport-teams/{teamId}
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> delete(@PathVariable String teamId) {
        try {
            service.delete(teamId);
            return ResponseEntity.ok(Map.of("message", "Team deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
