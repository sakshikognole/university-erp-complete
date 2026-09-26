package com.example.demo.schedule.controller;

import com.example.demo.schedule.model.Schedule;
import com.example.demo.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // GET /api/schedules?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<Schedule>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(scheduleService.getAll(page, size));
    }

    // GET /api/schedules/{scheduleId}
    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getOne(@PathVariable String scheduleId) {
        try {
            return ResponseEntity.ok(scheduleService.getByScheduleId(scheduleId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/schedules
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Schedule schedule) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(scheduleService.create(schedule));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/schedules/{scheduleId}
    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> update(
            @PathVariable String scheduleId,
            @Valid @RequestBody Schedule schedule) {
        try {
            return ResponseEntity.ok(scheduleService.update(scheduleId, schedule));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/schedules/{scheduleId}
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> delete(@PathVariable String scheduleId) {
        try {
            scheduleService.delete(scheduleId);
            return ResponseEntity.ok(Map.of("message", "Schedule deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
