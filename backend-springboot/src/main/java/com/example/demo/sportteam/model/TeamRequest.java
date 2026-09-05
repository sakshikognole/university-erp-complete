package com.example.demo.sportteam.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "team_requests")
public class TeamRequest {

    @Id
    private String id;

    @Indexed(unique = true)
    private String requestId;

    private String teamId;
    private String studentPrn;
    private String studentName;

    // PENDING | ACCEPTED | IGNORED
    private String status = "PENDING";

    public TeamRequest() {}

    public String getId()                      { return id; }
    public void   setId(String v)              { this.id = v; }
    public String getRequestId()               { return requestId; }
    public void   setRequestId(String v)       { this.requestId = v; }
    public String getTeamId()                  { return teamId; }
    public void   setTeamId(String v)          { this.teamId = v; }
    public String getStudentPrn()              { return studentPrn; }
    public void   setStudentPrn(String v)      { this.studentPrn = v; }
    public String getStudentName()             { return studentName; }
    public void   setStudentName(String v)     { this.studentName = v; }
    public String getStatus()                  { return status; }
    public void   setStatus(String v)          { this.status = v; }
}
