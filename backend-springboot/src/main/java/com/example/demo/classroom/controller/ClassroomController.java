package com.example.demo.classroom.controller;

import com.example.demo.classroom.model.Classroom;
import com.example.demo.classroom.service.ClassroomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService service;

    // ── Get all (with pagination) ──────────────────────────────────────────
    @GetMapping
    public ResponseEntity<Page<Classroom>> getAllClassrooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("classroomId").ascending());
        Page<Classroom> classrooms = service.getAllClassrooms(pageable);
        return ResponseEntity.ok(classrooms);
    }

    // ── Get by ID ──────────────────────────────────────────────────────────
    @GetMapping("/{classroomId}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable String classroomId) {
        Classroom classroom = service.getByClassroomId(classroomId);
        return ResponseEntity.ok(classroom);
    }

    // ── Create ─────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Classroom> createClassroom(@Valid @RequestBody Classroom classroom) {
        Classroom created = service.createClassroom(classroom);
        return ResponseEntity.ok(created);
    }

    // ── Update ─────────────────────────────────────────────────────────────
    @PutMapping("/{classroomId}")
    public ResponseEntity<Classroom> updateClassroom(
            @PathVariable String classroomId,
            @Valid @RequestBody Classroom classroom
    ) {
        Classroom updated = service.updateClassroom(classroomId, classroom);
        return ResponseEntity.ok(updated);
    }

    // ── Delete ─────────────────────────────────────────────────────────────
    @DeleteMapping("/{classroomId}")
    public ResponseEntity<String> deleteClassroom(@PathVariable String classroomId) {
        service.deleteClassroom(classroomId);
        return ResponseEntity.ok("Classroom deleted successfully");
    }
}
