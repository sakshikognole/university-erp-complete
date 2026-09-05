package com.example.demo.student.controller;

import com.example.demo.student.model.LetterHead;
import com.example.demo.student.service.LetterHeadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/letterhead")
public class LetterHeadController {

    private final LetterHeadService letterHeadService;

    public LetterHeadController(LetterHeadService letterHeadService) {
        this.letterHeadService = letterHeadService;
    }

    /** GET /api/letterhead — returns current letterhead settings */
    @GetMapping
    public ResponseEntity<LetterHead> get() {
        return ResponseEntity.ok(letterHeadService.get());
    }

    /** PUT /api/letterhead — save/update letterhead settings */
    @PutMapping
    public ResponseEntity<LetterHead> save(@RequestBody LetterHead letterHead) {
        return ResponseEntity.ok(letterHeadService.save(letterHead));
    }
}
