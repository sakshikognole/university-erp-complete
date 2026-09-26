package com.example.demo.subject.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subjects")
public class Subject {

    @Id
    private String id;

    // Auto-generated: SUB-001, SUB-002 ...
    @Indexed(unique = true)
    private String subjectId;

    @NotBlank(message = "Subject name is required")
    private String subjectName;

    private String description;

    public Subject() {}

    public String getId()                  { return id; }
    public void   setId(String v)          { this.id = v; }
    public String getSubjectId()           { return subjectId; }
    public void   setSubjectId(String v)   { this.subjectId = v; }
    public String getSubjectName()         { return subjectName; }
    public void   setSubjectName(String v) { this.subjectName = v; }
    public String getDescription()         { return description; }
    public void   setDescription(String v) { this.description = v; }
}
