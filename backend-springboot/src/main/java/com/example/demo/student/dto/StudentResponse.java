package com.example.demo.student.dto;

import com.example.demo.student.model.Student;

public class StudentResponse {

    private String studentId;
    private String studentName;
    private String gender;
    private String prn;
    private String division;
    private String yearOfEnrollment;
    private String studyingYear;
    private String degreeProgramName;
    private String academicYear;

    public StudentResponse() {}

    public StudentResponse(Student s) {
        this.studentId         = s.getStudentId();
        this.studentName       = s.getStudentName();
        this.gender            = s.getGender();
        this.prn               = s.getPrn();
        this.division          = s.getDivision();
        this.yearOfEnrollment  = s.getYearOfEnrollment();
        this.studyingYear      = s.getStudyingYear();
        this.degreeProgramName = s.getDegreeProgramName();
        this.academicYear      = s.getAcademicYear();
    }

    public String getStudentId()                               { return studentId; }
    public void   setStudentId(String v)                       { this.studentId = v; }
    public String getStudentName()                             { return studentName; }
    public void   setStudentName(String v)                     { this.studentName = v; }
    public String getGender()                                  { return gender; }
    public void   setGender(String v)                          { this.gender = v; }
    public String getPrn()                                     { return prn; }
    public void   setPrn(String v)                             { this.prn = v; }
    public String getDivision()                                { return division; }
    public void   setDivision(String v)                        { this.division = v; }
    public String getYearOfEnrollment()                        { return yearOfEnrollment; }
    public void   setYearOfEnrollment(String v)                { this.yearOfEnrollment = v; }
    public String getStudyingYear()                            { return studyingYear; }
    public void   setStudyingYear(String v)                    { this.studyingYear = v; }
    public String getDegreeProgramName()                       { return degreeProgramName; }
    public void   setDegreeProgramName(String v)               { this.degreeProgramName = v; }
    public String getAcademicYear()                            { return academicYear; }
    public void   setAcademicYear(String v)                    { this.academicYear = v; }
}
