package com.example.demo.venuebooking.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "venue_bookings")
public class VenueBooking {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Booking ID is required")
    private String bookingId;

    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "Venue ID is required")
    private String venueId;

    @NotNull(message = "Booking date is required")
    private String bookingDate;   // stored as "YYYY-MM-DD"

    @NotBlank(message = "Start time is required")
    private String startTime;     // stored as "HH:mm"

    @NotBlank(message = "End time is required")
    private String endTime;       // stored as "HH:mm"

    private String purpose;

    @NotBlank(message = "Requested by is required")
    private String requestedBy;

    @Pattern(regexp = "PENDING|APPROVED|REJECTED|CANCELLED",
             message = "Status must be PENDING, APPROVED, REJECTED, or CANCELLED")
    private String status = "PENDING";

    public VenueBooking() {}

    // Getters and setters
    public String getId()                    { return id; }
    public void   setId(String id)           { this.id = id; }
    public String getBookingId()             { return bookingId; }
    public void   setBookingId(String v)     { this.bookingId = v; }
    public String getEventId()               { return eventId; }
    public void   setEventId(String v)       { this.eventId = v; }
    public String getVenueId()               { return venueId; }
    public void   setVenueId(String v)       { this.venueId = v; }
    public String getBookingDate()           { return bookingDate; }
    public void   setBookingDate(String v)   { this.bookingDate = v; }
    public String getStartTime()             { return startTime; }
    public void   setStartTime(String v)     { this.startTime = v; }
    public String getEndTime()               { return endTime; }
    public void   setEndTime(String v)       { this.endTime = v; }
    public String getPurpose()               { return purpose; }
    public void   setPurpose(String v)       { this.purpose = v; }
    public String getRequestedBy()           { return requestedBy; }
    public void   setRequestedBy(String v)   { this.requestedBy = v; }
    public String getStatus()                { return status; }
    public void   setStatus(String v)        { this.status = v; }
}
