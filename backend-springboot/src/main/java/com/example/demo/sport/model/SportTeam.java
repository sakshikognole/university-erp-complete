package com.example.demo.sport.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "sport_teams")
public class SportTeam {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Team ID is required")
    private String teamId;

    @NotBlank(message = "Team name is required")
    private String teamName;

    @NotBlank(message = "Sport ID is required")
    private String sportId;

    @NotBlank(message = "Coach name is required")
    private String coachName;

    // List of member names / PRNs
    private List<String> members = new ArrayList<>();

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE or INACTIVE")
    private String status = "ACTIVE";

    private String description;

    public SportTeam() {}

    public SportTeam(String teamId, String teamName, String sportId,
                     String coachName, List<String> members,
                     String status, String description) {
        this.teamId      = teamId;
        this.teamName    = teamName;
        this.sportId     = sportId;
        this.coachName   = coachName;
        this.members     = members != null ? members : new ArrayList<>();
        this.status      = status != null ? status : "ACTIVE";
        this.description = description;
    }

    // Getters and setters
    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }
    public String getTeamId()                      { return teamId; }
    public void   setTeamId(String v)              { this.teamId = v; }
    public String getTeamName()                    { return teamName; }
    public void   setTeamName(String v)            { this.teamName = v; }
    public String getSportId()                     { return sportId; }
    public void   setSportId(String v)             { this.sportId = v; }
    public String getCoachName()                   { return coachName; }
    public void   setCoachName(String v)           { this.coachName = v; }
    public List<String> getMembers()               { return members; }
    public void         setMembers(List<String> v) { this.members = v; }
    public String getStatus()                      { return status; }
    public void   setStatus(String v)              { this.status = v; }
    public String getDescription()                 { return description; }
    public void   setDescription(String v)         { this.description = v; }
}
