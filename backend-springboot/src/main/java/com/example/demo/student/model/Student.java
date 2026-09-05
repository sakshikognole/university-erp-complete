package com.example.demo.student.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
public class Student {

    @Id
    private String id;

    @Indexed(unique = true)
    private String studentId;

    private String studentName;
    private String gender;
    private String prn;
    private String division;
    private String yearOfEnrollment;
    private String studyingYear;
    private String degreeProgramName;
    private String academicYear;

    public Student() {}

    public Student(String studentId, String studentName, String gender,
                   String prn, String division, String yearOfEnrollment,
                   String studyingYear, String degreeProgramName, String academicYear) {
        this.studentId        = studentId;
        this.studentName      = studentName;
        this.gender           = gender;
        this.prn              = prn;
        this.division         = division;
        this.yearOfEnrollment = yearOfEnrollment;
        this.studyingYear     = studyingYear;
        this.degreeProgramName = degreeProgramName;
        this.academicYear     = academicYear;
    }

    public String getId()                                        { return id; }
    public void   setId(String id)                               { this.id = id; }
    public String getStudentId()                                 { return studentId; }
    public void   setStudentId(String studentId)                 { this.studentId = studentId; }
    public String getStudentName()                               { return studentName; }
    public void   setStudentName(String studentName)             { this.studentName = studentName; }
    public String getGender()                                    { return gender; }
    public void   setGender(String gender)                       { this.gender = gender; }
    public String getPrn()                                       { return prn; }
    public void   setPrn(String prn)                             { this.prn = prn; }
    public String getDivision()                                  { return division; }
    public void   setDivision(String division)                   { this.division = division; }
    public String getYearOfEnrollment()                          { return yearOfEnrollment; }
    public void   setYearOfEnrollment(String yearOfEnrollment)   { this.yearOfEnrollment = yearOfEnrollment; }
    public String getStudyingYear()                              { return studyingYear; }
    public void   setStudyingYear(String studyingYear)           { this.studyingYear = studyingYear; }
    public String getDegreeProgramName()                         { return degreeProgramName; }
    public void   setDegreeProgramName(String degreeProgramName) { this.degreeProgramName = degreeProgramName; }
    public String getAcademicYear()                              { return academicYear; }
    public void   setAcademicYear(String academicYear)           { this.academicYear = academicYear; }
}
