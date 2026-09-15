package com.example.demo.exam.controller;

import com.example.demo.exam.model.Exam;
import com.example.demo.exam.model.Exam.StudentMark;
import com.example.demo.exam.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService service;

    public ExamController(ExamService service) {
        this.service = service;
    }

    // GET /api/exams
    @GetMapping
    public ResponseEntity<List<Exam>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET /api/exams/{examId}
    @GetMapping("/{examId}")
    public ResponseEntity<?> getOne(@PathVariable String examId) {
        try {
            return ResponseEntity.ok(service.getByExamId(examId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/exams
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Exam exam) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(exam));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/exams/{examId}
    @PutMapping("/{examId}")
    public ResponseEntity<?> update(@PathVariable String examId,
                                    @Valid @RequestBody Exam exam) {
        try {
            return ResponseEntity.ok(service.update(examId, exam));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/exams/{examId}/marks/{section}  — section = t1 | t2 | see
    @PutMapping("/{examId}/marks/{section}")
    public ResponseEntity<?> saveMarks(@PathVariable String examId,
                                       @PathVariable String section,
                                       @RequestBody List<StudentMark> marks) {
        try {
            return ResponseEntity.ok(service.saveMarksForSection(examId, section, marks));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/exams/{examId}
    @DeleteMapping("/{examId}")
    public ResponseEntity<?> delete(@PathVariable String examId) {
        try {
            service.delete(examId);
            return ResponseEntity.ok(Map.of("message", "Exam deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
