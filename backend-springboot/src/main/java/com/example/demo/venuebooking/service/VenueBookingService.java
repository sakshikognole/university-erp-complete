package com.example.demo.venuebooking.service;

import com.example.demo.venuebooking.model.VenueBooking;
import com.example.demo.venuebooking.repository.VenueBookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class VenueBookingService {

    private final VenueBookingRepository repo;

    public VenueBookingService(VenueBookingRepository repo) {
        this.repo = repo;
    }

    // ── GET ALL ──────────────────────────────────────────────────────────
    public List<VenueBooking> getAll() {
        return repo.findAllByOrderByBookingDateDesc();
    }

    // ── GET ONE ──────────────────────────────────────────────────────────
    public VenueBooking getByBookingId(String bookingId) {
        return repo.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Booking not found with ID: " + bookingId));
    }

    // ── CREATE ────────────────────────────────────────────────────────────
    public VenueBooking create(VenueBooking booking) {

        // 1. Unique bookingId
        if (repo.existsByBookingId(booking.getBookingId().trim())) {
            throw new IllegalArgumentException(
                    "Booking ID '" + booking.getBookingId() + "' already exists.");
        }

        // 2. Time validation
        validateTimes(booking.getStartTime(), booking.getEndTime());

        // 3. Overlap check
        List<VenueBooking> overlaps = repo.findOverlapping(
                booking.getVenueId(), booking.getBookingDate(),
                booking.getStartTime(), booking.getEndTime());
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException(
                    "This venue is already booked for the selected date and time.");
        }

        // 4. Default status
        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("PENDING");
        }

        booking.setBookingId(booking.getBookingId().trim());
        return repo.save(booking);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public VenueBooking update(String bookingId, VenueBooking incoming) {
        VenueBooking existing = getByBookingId(bookingId);

        // Time validation
        validateTimes(incoming.getStartTime(), incoming.getEndTime());

        // Overlap check (exclude self)
        List<VenueBooking> overlaps = repo.findOverlappingExcluding(
                incoming.getVenueId(), incoming.getBookingDate(),
                incoming.getStartTime(), incoming.getEndTime(), bookingId);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException(
                    "This venue is already booked for the selected date and time.");
        }

        existing.setEventId(incoming.getEventId());
        existing.setVenueId(incoming.getVenueId());
        existing.setBookingDate(incoming.getBookingDate());
        existing.setStartTime(incoming.getStartTime());
        existing.setEndTime(incoming.getEndTime());
        existing.setPurpose(incoming.getPurpose());
        existing.setRequestedBy(incoming.getRequestedBy());
        existing.setStatus(incoming.getStatus());
        return repo.save(existing);
    }

    // ── PATCH STATUS ──────────────────────────────────────────────────────
    public VenueBooking updateStatus(String bookingId, String newStatus) {
        VenueBooking existing = getByBookingId(bookingId);
        existing.setStatus(newStatus.toUpperCase());
        return repo.save(existing);
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public void delete(String bookingId) {
        repo.delete(getByBookingId(bookingId));
    }

    // ── HELPERS ───────────────────────────────────────────────────────────
    private void validateTimes(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end   = LocalTime.parse(endTime);
            if (!end.isAfter(start)) {
                throw new IllegalArgumentException(
                        "End time must be after start time.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid time format. Use HH:mm.");
        }
    }
}
