package com.example.demo.sportteam.controller;

import com.example.demo.sportteam.model.SportTeam;
import com.example.demo.sportteam.model.TeamRequest;
import com.example.demo.sportteam.service.SportTeamService;
import com.example.demo.sportteam.service.TeamRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sport-teams")
public class SportTeamController {

    private final SportTeamService sportTeamService;
    private final TeamRequestService teamRequestService;

    public SportTeamController(SportTeamService sportTeamService,
                               TeamRequestService teamRequestService) {
        this.sportTeamService   = sportTeamService;
        this.teamRequestService = teamRequestService;
    }

    // GET /api/sport-teams
    @GetMapping
    public ResponseEntity<List<SportTeam>> getAll() {
        return ResponseEntity.ok(sportTeamService.getAll());
    }

    // GET /api/sport-teams/{teamId}
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getOne(@PathVariable String teamId) {
        try {
            return ResponseEntity.ok(sportTeamService.getByTeamId(teamId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/sport-teams
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SportTeam team) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(sportTeamService.create(team));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/sport-teams/{teamId}
    @PutMapping("/{teamId}")
    public ResponseEntity<?> update(@PathVariable String teamId,
                                    @RequestBody SportTeam team) {
        try {
            return ResponseEntity.ok(sportTeamService.update(teamId, team));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/sport-teams/{teamId}
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> delete(@PathVariable String teamId) {
        try {
            sportTeamService.delete(teamId);
            return ResponseEntity.ok(Map.of("message", "Sport team deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/sport-teams/{teamId}/roster/add
    @PostMapping("/{teamId}/roster/add")
    public ResponseEntity<?> addToRoster(@PathVariable String teamId,
                                         @RequestBody Map<String, String> body) {
        try {
            String prn  = body.get("studentPrn");
            String name = body.get("studentName");
            return ResponseEntity.ok(sportTeamService.addToRoster(teamId, prn, name));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/sport-teams/{teamId}/roster/{studentPrn}
    @DeleteMapping("/{teamId}/roster/{studentPrn}")
    public ResponseEntity<?> removeFromRoster(@PathVariable String teamId,
                                              @PathVariable String studentPrn) {
        try {
            return ResponseEntity.ok(sportTeamService.removeFromRoster(teamId, studentPrn));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // GET /api/sport-teams/{teamId}/requests
    @GetMapping("/{teamId}/requests")
    public ResponseEntity<?> getRequests(@PathVariable String teamId) {
        try {
            List<TeamRequest> requests = teamRequestService.getByTeamId(teamId);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/sport-teams/{teamId}/requests
    @PostMapping("/{teamId}/requests")
    public ResponseEntity<?> createRequest(@PathVariable String teamId,
                                           @RequestBody Map<String, String> body) {
        try {
            String prn  = body.get("studentPrn");
            String name = body.get("studentName");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(teamRequestService.create(teamId, prn, name));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
