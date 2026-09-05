package com.example.demo.venuebooking.repository;

import com.example.demo.venuebooking.model.VenueBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueBookingRepository extends MongoRepository<VenueBooking, String> {

    Optional<VenueBooking> findByBookingId(String bookingId);

    boolean existsByBookingId(String bookingId);

    List<VenueBooking> findAllByOrderByBookingDateDesc();

    /**
     * Find overlapping bookings for the same venue on the same date.
     * A booking overlaps when the existing booking's time range intersects
     * the requested time range, and the status is not CANCELLED or REJECTED.
     *
     * Overlap condition: existingStart < newEnd  AND  existingEnd > newStart
     */
    @Query("{ 'venueId': ?0, 'bookingDate': ?1, " +
           "'status': { $nin: ['CANCELLED', 'REJECTED'] }, " +
           "'startTime': { $lt: ?3 }, " +
           "'endTime':   { $gt: ?2 } }")
    List<VenueBooking> findOverlapping(String venueId, String bookingDate,
                                       String newStart, String newEnd);

    /**
     * Same as above but excludes a specific bookingId (used during updates).
     */
    @Query("{ 'venueId': ?0, 'bookingDate': ?1, " +
           "'bookingId': { $ne: ?4 }, " +
           "'status': { $nin: ['CANCELLED', 'REJECTED'] }, " +
           "'startTime': { $lt: ?3 }, " +
           "'endTime':   { $gt: ?2 } }")
    List<VenueBooking> findOverlappingExcluding(String venueId, String bookingDate,
                                                String newStart, String newEnd,
                                                String excludeBookingId);
}
