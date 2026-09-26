package com.example.demo.subject.controller;

import com.example.demo.subject.model.Subject;
import com.example.demo.subject.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // GET /api/subjects
    @GetMapping
    public ResponseEntity<List<Subject>> getAll() {
        return ResponseEntity.ok(subjectService.getAll());
    }

    // GET /api/subjects/{subjectId}
    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getOne(@PathVariable String subjectId) {
        try {
            return ResponseEntity.ok(subjectService.getBySubjectId(subjectId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/subjects  — subjectId is auto-generated, client sends only name + description
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Subject subject) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(subjectService.create(subject));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/subjects/{subjectId}
    @PutMapping("/{subjectId}")
    public ResponseEntity<?> update(
            @PathVariable String subjectId,
            @Valid @RequestBody Subject subject) {
        try {
            return ResponseEntity.ok(subjectService.update(subjectId, subject));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/subjects/{subjectId}
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> delete(@PathVariable String subjectId) {
        try {
            subjectService.delete(subjectId);
            return ResponseEntity.ok(Map.of("message", "Subject deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
