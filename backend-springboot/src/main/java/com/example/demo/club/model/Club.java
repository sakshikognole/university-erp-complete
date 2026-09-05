package com.example.demo.club.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clubs")
public class Club {

    @Id
    private String id;

    @Indexed(unique = true)
    private String clubId;

    private String clubName;
    private String clubCategory;

    // Empty or null means this is an independent club (not a sub-club)
    private String parentClubId;

    private String description;
    private String facultyCoordinator;

    // Student lead details
    private String studentLeadName;
    private String studentLeadId;
    private String studentLeadRole;

    private Integer activeMembers;

    // "Active" or "Inactive"
    private String status;

    public Club() {}

    public String getId()                              { return id; }
    public void   setId(String id)                     { this.id = id; }
    public String getClubId()                          { return clubId; }
    public void   setClubId(String v)                  { this.clubId = v; }
    public String getClubName()                        { return clubName; }
    public void   setClubName(String v)                { this.clubName = v; }
    public String getClubCategory()                    { return clubCategory; }
    public void   setClubCategory(String v)            { this.clubCategory = v; }
    public String getParentClubId()                    { return parentClubId; }
    public void   setParentClubId(String v)            { this.parentClubId = v; }
    public String getDescription()                     { return description; }
    public void   setDescription(String v)             { this.description = v; }
    public String getFacultyCoordinator()              { return facultyCoordinator; }
    public void   setFacultyCoordinator(String v)      { this.facultyCoordinator = v; }
    public String getStudentLeadName()                 { return studentLeadName; }
    public void   setStudentLeadName(String v)         { this.studentLeadName = v; }
    public String getStudentLeadId()                   { return studentLeadId; }
    public void   setStudentLeadId(String v)           { this.studentLeadId = v; }
    public String getStudentLeadRole()                 { return studentLeadRole; }
    public void   setStudentLeadRole(String v)         { this.studentLeadRole = v; }
    public Integer getActiveMembers()                  { return activeMembers; }
    public void    setActiveMembers(Integer v)         { this.activeMembers = v; }
    public String getStatus()                          { return status; }
    public void   setStatus(String v)                  { this.status = v; }
}
