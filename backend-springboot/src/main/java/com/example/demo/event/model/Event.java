package com.example.demo.event.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "events")
public class Event {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "Event title is required")
    private String eventTitle;

    private String eventType;

    private String organizerId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String venueId;

    // Hidden field — stored but not exposed in frontend
    private Double budget;

    private String description;

    // ACTIVE or INACTIVE
    private String status;

    public Event() {}

    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }
    public String getEventId()                     { return eventId; }
    public void   setEventId(String v)             { this.eventId = v; }
    public String getEventTitle()                  { return eventTitle; }
    public void   setEventTitle(String v)          { this.eventTitle = v; }
    public String getEventType()                   { return eventType; }
    public void   setEventType(String v)           { this.eventType = v; }
    public String getOrganizerId()                 { return organizerId; }
    public void   setOrganizerId(String v)         { this.organizerId = v; }
    public LocalDate getStartDate()                { return startDate; }
    public void      setStartDate(LocalDate v)     { this.startDate = v; }
    public LocalDate getEndDate()                  { return endDate; }
    public void      setEndDate(LocalDate v)       { this.endDate = v; }
    public String getVenueId()                     { return venueId; }
    public void   setVenueId(String v)             { this.venueId = v; }
    public Double getBudget()                      { return budget; }
    public void   setBudget(Double v)              { this.budget = v; }
    public String getDescription()                 { return description; }
    public void   setDescription(String v)         { this.description = v; }
    public String getStatus()                      { return status; }
    public void   setStatus(String v)              { this.status = v; }
}
