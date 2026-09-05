package com.example.demo.sportteam.model;

public class RosterEntry {

    private String studentPrn;
    private String studentName;

    public RosterEntry() {}

    public RosterEntry(String studentPrn, String studentName) {
        this.studentPrn  = studentPrn;
        this.studentName = studentName;
    }

    public String getStudentPrn()              { return studentPrn; }
    public void   setStudentPrn(String v)      { this.studentPrn = v; }
    public String getStudentName()             { return studentName; }
    public void   setStudentName(String v)     { this.studentName = v; }
}
