package com.example.demo.classroom.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "classrooms")
public class Classroom {

    @Id
    private String id;

    // Auto-generated: CLS-001, CLS-002 ...
    @Indexed(unique = true)
    private String classroomId;

    // Classroom name: alphanumeric only (e.g., "Room101", "Hall A", "Lab 3")
    @NotBlank(message = "Classroom name is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Classroom name must contain only letters, numbers and spaces")
    private String classroomName;

    @Min(value = 1, message = "Rows must be at least 1")
    private int rows;

    @Min(value = 1, message = "Columns must be at least 1")
    private int columns;

    @Min(value = 0, message = "Extra cannot be negative")
    private int extra;

    // Capacity = rows * columns + extra (calculated field)
    private int capacity;

    // Examiner: faculty ID
    private String examinerId;

    // Examiner name (denormalized for display)
    private String examinerName;

    public Classroom() {}

    // ── Getters / Setters ─────────────────────────────────────────────────
    public String getId()                  { return id; }
    public void   setId(String v)          { this.id = v; }

    public String getClassroomId()         { return classroomId; }
    public void   setClassroomId(String v) { this.classroomId = v; }

    public String getClassroomName()       { return classroomName; }
    public void   setClassroomName(String v) { this.classroomName = v; }

    public int    getRows()                { return rows; }
    public void   setRows(int v)           { this.rows = v; }

    public int    getColumns()             { return columns; }
    public void   setColumns(int v)        { this.columns = v; }

    public int    getExtra()               { return extra; }
    public void   setExtra(int v)          { this.extra = v; }

    public int    getCapacity()            { return capacity; }
    public void   setCapacity(int v)       { this.capacity = v; }

    public String getExaminerId()          { return examinerId; }
    public void   setExaminerId(String v)  { this.examinerId = v; }

    public String getExaminerName()        { return examinerName; }
    public void   setExaminerName(String v) { this.examinerName = v; }
}
