package com.example.demo.venuebooking.controller;

import com.example.demo.venuebooking.model.VenueBooking;
import com.example.demo.venuebooking.service.VenueBookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/venue-bookings")
public class VenueBookingController {

    private final VenueBookingService service;

    public VenueBookingController(VenueBookingService service) {
        this.service = service;
    }

    // GET /api/venue-bookings
    @GetMapping
    public ResponseEntity<List<VenueBooking>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET /api/venue-bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getOne(@PathVariable String bookingId) {
        try {
            return ResponseEntity.ok(service.getByBookingId(bookingId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/venue-bookings
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VenueBooking booking) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(booking));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/venue-bookings/{bookingId}
    @PutMapping("/{bookingId}")
    public ResponseEntity<?> update(
            @PathVariable String bookingId,
            @Valid @RequestBody VenueBooking booking) {
        try {
            return ResponseEntity.ok(service.update(bookingId, booking));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PATCH /api/venue-bookings/{bookingId}/status
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String bookingId,
            @RequestBody Map<String, String> body) {
        try {
            String newStatus = body.get("status");
            if (newStatus == null || newStatus.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status is required."));
            }
            return ResponseEntity.ok(service.updateStatus(bookingId, newStatus));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/venue-bookings/{bookingId}
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> delete(@PathVariable String bookingId) {
        try {
            service.delete(bookingId);
            return ResponseEntity.ok(
                    Map.of("message", "Booking deleted successfully."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
