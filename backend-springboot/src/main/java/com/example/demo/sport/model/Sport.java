package com.example.demo.sport.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sports")
public class Sport {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Sport ID is required")
    private String sportId;

    @NotBlank(message = "Sport name is required")
    private String sportName;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE or INACTIVE")
    private String status;

    private String description;

    @NotBlank(message = "Venue ID is required")
    private String venueId;

    public Sport() {}

    public String getId()                      { return id; }
    public void   setId(String id)             { this.id = id; }
    public String getSportId()                 { return sportId; }
    public void   setSportId(String v)         { this.sportId = v; }
    public String getSportName()               { return sportName; }
    public void   setSportName(String v)       { this.sportName = v; }
    public Integer getCapacity()               { return capacity; }
    public void    setCapacity(Integer v)      { this.capacity = v; }
    public String getStatus()                  { return status; }
    public void   setStatus(String v)          { this.status = v; }
    public String getDescription()             { return description; }
    public void   setDescription(String v)     { this.description = v; }
    public String getVenueId()                 { return venueId; }
    public void   setVenueId(String v)         { this.venueId = v; }
}
