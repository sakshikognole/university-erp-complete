package com.example.demo.club.controller;

import com.example.demo.club.model.Club;
import com.example.demo.club.service.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    // GET /api/clubs
    @GetMapping
    public ResponseEntity<List<Club>> getAll() {
        return ResponseEntity.ok(clubService.getAll());
    }

    // POST /api/clubs
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Club club) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(clubService.create(club));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/clubs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Club club) {
        try {
            return ResponseEntity.ok(clubService.update(id, club));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/clubs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        clubService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Club deleted successfully."));
    }
}
