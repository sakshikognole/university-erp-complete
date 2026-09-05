package com.example.demo.hostel.controller;

import com.example.demo.hostel.model.HostelRoom;
import com.example.demo.hostel.service.HostelRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hostel-rooms")
public class HostelRoomController {

    private final HostelRoomService service;

    public HostelRoomController(HostelRoomService service) {
        this.service = service;
    }

    /**
     * GET /api/hostel-rooms?blockId=HB001
     * Returns only rooms that belong to the given blockId.
     */
    @GetMapping
    public ResponseEntity<?> getByBlock(@RequestParam String blockId) {
        return ResponseEntity.ok(service.getByBlock(blockId));
    }

    // POST /api/hostel-rooms
    @PostMapping
    public ResponseEntity<?> create(@RequestBody HostelRoom room) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(room));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/hostel-rooms/{roomId}
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> delete(@PathVariable String roomId) {
        try {
            service.delete(roomId);
            return ResponseEntity.ok(Map.of("message", "Room deleted."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/hostel-rooms/{roomId}/students
    // Body: { "studentPrn": "2021001", "studentName": "Rahul Patil" }
    @PostMapping("/{roomId}/students")
    public ResponseEntity<?> addStudent(
            @PathVariable String roomId,
            @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(
                    service.addStudent(roomId,
                            body.get("studentPrn"),
                            body.get("studentName")));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/hostel-rooms/{roomId}/students/{prn}
    @DeleteMapping("/{roomId}/students/{prn}")
    public ResponseEntity<?> removeStudent(
            @PathVariable String roomId,
            @PathVariable String prn) {
        try {
            return ResponseEntity.ok(service.removeStudent(roomId, prn));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
