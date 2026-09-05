package com.example.demo.hostel.controller;

import com.example.demo.hostel.model.HostelBlock;
import com.example.demo.hostel.service.HostelBlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hostel-blocks")
public class HostelBlockController {

    private final HostelBlockService service;

    public HostelBlockController(HostelBlockService service) {
        this.service = service;
    }

    // GET /api/hostel-blocks?page=0&size=6
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(service.getAll(page, size));
    }

    // GET /api/hostel-blocks/all  (flat list for dropdowns)
    @GetMapping("/all")
    public ResponseEntity<?> getAllFlat() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET /api/hostel-blocks/{blockId}
    @GetMapping("/{blockId}")
    public ResponseEntity<?> getOne(@PathVariable String blockId) {
        try {
            return ResponseEntity.ok(service.getByBlockId(blockId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/hostel-blocks
    @PostMapping
    public ResponseEntity<?> create(@RequestBody HostelBlock block) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(block));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/hostel-blocks/{blockId}
    @PutMapping("/{blockId}")
    public ResponseEntity<?> update(@PathVariable String blockId,
                                    @RequestBody HostelBlock block) {
        try {
            return ResponseEntity.ok(service.update(blockId, block));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/hostel-blocks/{blockId}
    @DeleteMapping("/{blockId}")
    public ResponseEntity<?> delete(@PathVariable String blockId) {
        try {
            service.delete(blockId);
            return ResponseEntity.ok(Map.of("message", "Hostel block deleted."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
