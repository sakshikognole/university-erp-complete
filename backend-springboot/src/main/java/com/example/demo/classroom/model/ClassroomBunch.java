package com.example.demo.classroom.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "classroom_bunches")
public class ClassroomBunch {

    @Id
    private String id;

    // Auto-generated: BUNCH-001, BUNCH-002, ...
    @Indexed(unique = true)
    private String bunchId;

    // Bunch name given by user (e.g., "Engineering Block A")
    @NotBlank(message = "Bunch name is required")
    private String bunchName;

    // List of classroom IDs in this bunch
    @NotEmpty(message = "At least one classroom must be selected")
    private List<String> classroomIds;

    // Total classrooms (calculated)
    private int totalClassrooms;

    // Total capacity (sum of all classroom capacities)
    private int totalCapacity;

    public ClassroomBunch() {}

    // ── Getters / Setters ─────────────────────────────────────────────────
    public String getId()                       { return id; }
    public void   setId(String v)               { this.id = v; }

    public String getBunchId()                  { return bunchId; }
    public void   setBunchId(String v)          { this.bunchId = v; }

    public String getBunchName()                { return bunchName; }
    public void   setBunchName(String v)        { this.bunchName = v; }

    public List<String> getClassroomIds()      { return classroomIds; }
    public void   setClassroomIds(List<String> v) { this.classroomIds = v; }

    public int    getTotalClassrooms()          { return totalClassrooms; }
    public void   setTotalClassrooms(int v)     { this.totalClassrooms = v; }

    public int    getTotalCapacity()            { return totalCapacity; }
    public void   setTotalCapacity(int v)       { this.totalCapacity = v; }
}
