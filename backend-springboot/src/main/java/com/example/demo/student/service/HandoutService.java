package com.example.demo.student.service;

import com.example.demo.student.model.Student;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class HandoutService {

    private final StudentService     studentService;
    private final CertificateService certificateService;

    public HandoutService(StudentService studentService,
                          CertificateService certificateService) {
        this.studentService     = studentService;
        this.certificateService = certificateService;
    }

    public byte[] generateHandoutZip(List<String> studentIds, List<String> documentTypes)
            throws IOException, DocumentException {
        return generateHandoutZip(studentIds, documentTypes, null);
    }

    public byte[] generateHandoutZip(List<String> studentIds, List<String> documentTypes, List<Map<String, Object>> studentsData)
            throws IOException, DocumentException {

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(zipBytes)) {
            if (studentsData != null && !studentsData.isEmpty()) {
                for (Map<String, Object> sMap : studentsData) {
                    String studentId         = String.valueOf(sMap.getOrDefault("studentId", ""));
                    String studentName       = String.valueOf(sMap.getOrDefault("studentName", sMap.getOrDefault("name", studentId)));
                    String degreeProgramName = String.valueOf(sMap.getOrDefault("degreeProgramName", sMap.getOrDefault("degree", "")));
                    String studyingYear      = String.valueOf(sMap.getOrDefault("studyingYear", sMap.getOrDefault("class", "")));
                    String academicYear      = String.valueOf(sMap.getOrDefault("academicYear", sMap.getOrDefault("yearOfEnrollment", "")));
                    String gender            = String.valueOf(sMap.getOrDefault("gender", ""));

                    String safeName = (studentName != null && !studentName.isBlank() ? studentName : studentId).replaceAll("\\s+", "_");
                    for (String docType : documentTypes) {
                        byte[] pdfBytes = certificateService.generatePdfWithDetails(
                                studentId, studentName, degreeProgramName,
                                studyingYear, academicYear, gender, null, docType
                        );
                        String safeDoc  = docType.replaceAll("\\s+", "_");
                        String fileName = safeName + "_" + safeDoc + ".pdf";
                        ZipEntry entry  = new ZipEntry(fileName);
                        zos.putNextEntry(entry);
                        zos.write(pdfBytes);
                        zos.closeEntry();
                    }
                }
            } else if (studentIds != null) {
                for (String studentId : studentIds) {
                    Student student = studentService.getStudentModelById(studentId);
                    String safeName = (student.getStudentName() != null ? student.getStudentName() : studentId).replaceAll("\\s+", "_");
                    for (String docType : documentTypes) {
                        byte[] pdfBytes = certificateService.generatePdf(studentId);
                        String safeDoc  = docType.replaceAll("\\s+", "_");
                        String fileName = safeName + "_" + safeDoc + ".pdf";
                        ZipEntry entry  = new ZipEntry(fileName);
                        zos.putNextEntry(entry);
                        zos.write(pdfBytes);
                        zos.closeEntry();
                    }
                }
            }
        }
        return zipBytes.toByteArray();
    }
}
