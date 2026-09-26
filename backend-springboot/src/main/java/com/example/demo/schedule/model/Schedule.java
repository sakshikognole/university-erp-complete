package com.example.demo.schedule.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "schedules")
public class Schedule {

    @Id
    private String id;

    // Auto-generated: SCH-001, SCH-002 ...
    @Indexed(unique = true)
    private String scheduleId;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotBlank(message = "Subject is required")
    private String subjectId;

    private String subjectName;   // denormalized for display

    @NotBlank(message = "Faculty is required")
    private String facultyId;

    private String facultyName;   // denormalized for display

    @NotBlank(message = "Venue is required")
    private String venueId;

    private String venueName;     // denormalized for display

    // Each entry: { day: "M", startTime: "09:00", endTime: "10:00" }
    private List<DaySlot> daySlots;

    public Schedule() {}

    // ── Getters / Setters ─────────────────────────────────────────────────
    public String getId()                        { return id; }
    public void   setId(String v)                { this.id = v; }
    public String getScheduleId()                { return scheduleId; }
    public void   setScheduleId(String v)        { this.scheduleId = v; }
    public String getDepartment()                { return department; }
    public void   setDepartment(String v)        { this.department = v; }
    public String getSemester()                  { return semester; }
    public void   setSemester(String v)          { this.semester = v; }
    public String getSubjectId()                 { return subjectId; }
    public void   setSubjectId(String v)         { this.subjectId = v; }
    public String getSubjectName()               { return subjectName; }
    public void   setSubjectName(String v)       { this.subjectName = v; }
    public String getFacultyId()                 { return facultyId; }
    public void   setFacultyId(String v)         { this.facultyId = v; }
    public String getFacultyName()               { return facultyName; }
    public void   setFacultyName(String v)       { this.facultyName = v; }
    public String getVenueId()                   { return venueId; }
    public void   setVenueId(String v)           { this.venueId = v; }
    public String getVenueName()                 { return venueName; }
    public void   setVenueName(String v)         { this.venueName = v; }
    public List<DaySlot> getDaySlots()           { return daySlots; }
    public void   setDaySlots(List<DaySlot> v)   { this.daySlots = v; }

    // ── Embedded DaySlot ──────────────────────────────────────────────────
    public static class DaySlot {
        private String day;        // M, T, W, Th, F, S, Su
        private String startTime;  // HH:mm
        private String endTime;    // HH:mm

        public DaySlot() {}
        public String getDay()                   { return day; }
        public void   setDay(String v)           { this.day = v; }
        public String getStartTime()             { return startTime; }
        public void   setStartTime(String v)     { this.startTime = v; }
        public String getEndTime()               { return endTime; }
        public void   setEndTime(String v)       { this.endTime = v; }
    }
}
