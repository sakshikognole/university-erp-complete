package com.example.demo.student.controller;

import com.example.demo.student.service.CertificateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final CertificateService certificateService;

    public DocumentController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    // POST /api/documents/bonafide  — custom content from frontend
    @PostMapping("/bonafide")
    public ResponseEntity<byte[]> generateWithContent(@RequestBody Map<String, String> body) {
        String studentId     = body.get("studentId");
        String customContent = body.get("customContent");
        String docType       = body.get("docType");

        if (studentId == null || studentId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            byte[] pdf = (customContent == null || customContent.isBlank())
                    ? certificateService.generatePdf(studentId)
                    : certificateService.generatePdfWithContent(studentId, customContent, docType);
            // FIX D8: use inline so browser renders PDF in iframe instead of forcing download
            return pdfInlineResponse(pdf, studentId);
        } catch (Exception e) {
            System.err.println("[DocumentController] PDF error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET /api/documents/bonafide/{studentId}  — used by HandoutService (force download)
    @GetMapping("/bonafide/{studentId}")
    public ResponseEntity<byte[]> generateDefault(@PathVariable String studentId) {
        try {
            return pdfAttachmentResponse(certificateService.generatePdf(studentId), studentId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // inline — browser previews PDF (used for certificate preview page)
    private ResponseEntity<byte[]> pdfInlineResponse(byte[] pdf, String studentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"certificate_" + studentId + ".pdf\"");
        headers.setContentLength(pdf.length);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    // attachment — browser downloads PDF (used for handout/bulk download)
    private ResponseEntity<byte[]> pdfAttachmentResponse(byte[] pdf, String studentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"certificate_" + studentId + ".pdf\"");
        headers.setContentLength(pdf.length);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
