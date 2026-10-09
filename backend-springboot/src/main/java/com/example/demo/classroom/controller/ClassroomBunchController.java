package com.example.demo.classroom.controller;

import com.example.demo.classroom.model.ClassroomBunch;
import com.example.demo.classroom.service.ClassroomBunchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom-bunches")
@CrossOrigin(origins = "*")
public class ClassroomBunchController {

    @Autowired
    private ClassroomBunchService bunchService;

    // Create bunch
    @PostMapping
    public ResponseEntity<?> createBunch(@Valid @RequestBody ClassroomBunch bunch) {
        try {
            ClassroomBunch created = bunchService.createBunch(bunch);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all bunches
    @GetMapping
    public ResponseEntity<List<ClassroomBunch>> getAllBunches() {
        return ResponseEntity.ok(bunchService.getAllBunches());
    }

    // Get bunch by ID
    @GetMapping("/{bunchId}")
    public ResponseEntity<?> getBunchById(@PathVariable String bunchId) {
        ClassroomBunch bunch = bunchService.getBunchById(bunchId);
        if (bunch == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bunch);
    }

    // Update bunch
    @PutMapping("/{bunchId}")
    public ResponseEntity<?> updateBunch(@PathVariable String bunchId, @Valid @RequestBody ClassroomBunch updates) {
        try {
            ClassroomBunch updated = bunchService.updateBunch(bunchId, updates);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete bunch
    @DeleteMapping("/{bunchId}")
    public ResponseEntity<?> deleteBunch(@PathVariable String bunchId) {
        try {
            bunchService.deleteBunch(bunchId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
