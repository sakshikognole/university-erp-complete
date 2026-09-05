package com.example.demo.sportteam.controller;

import com.example.demo.sportteam.service.TeamRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sport-team-requests")
public class TeamRequestController {

    private final TeamRequestService teamRequestService;

    public TeamRequestController(TeamRequestService teamRequestService) {
        this.teamRequestService = teamRequestService;
    }

    // PUT /api/sport-team-requests/{requestId}/accept
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<?> accept(@PathVariable String requestId) {
        try {
            return ResponseEntity.ok(teamRequestService.accept(requestId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/sport-team-requests/{requestId}/ignore
    @PutMapping("/{requestId}/ignore")
    public ResponseEntity<?> ignore(@PathVariable String requestId) {
        try {
            return ResponseEntity.ok(teamRequestService.ignore(requestId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
