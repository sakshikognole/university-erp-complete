package com.example.demo.student.service;

import com.example.demo.student.model.Student;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
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

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(zipBytes)) {
            for (String studentId : studentIds) {
                Student student = studentService.getStudentModelById(studentId);
                String safeName = student.getStudentName().replaceAll("\\s+", "_");
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
        return zipBytes.toByteArray();
    }
}
