package com.example.demo.sportteam.model;

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
    private String teamId;

    private String sportId;
    private String sportName;
    private String coachName;
    private String captainName;

    private List<RosterEntry> roster = new ArrayList<>();

    public SportTeam() {}

    public String getId()                          { return id; }
    public void   setId(String v)                  { this.id = v; }
    public String getTeamId()                      { return teamId; }
    public void   setTeamId(String v)              { this.teamId = v; }
    public String getSportId()                     { return sportId; }
    public void   setSportId(String v)             { this.sportId = v; }
    public String getSportName()                   { return sportName; }
    public void   setSportName(String v)           { this.sportName = v; }
    public String getCoachName()                   { return coachName; }
    public void   setCoachName(String v)           { this.coachName = v; }
    public String getCaptainName()                 { return captainName; }
    public void   setCaptainName(String v)         { this.captainName = v; }
    public List<RosterEntry> getRoster()           { return roster; }
    public void   setRoster(List<RosterEntry> v)   { this.roster = v; }
}
