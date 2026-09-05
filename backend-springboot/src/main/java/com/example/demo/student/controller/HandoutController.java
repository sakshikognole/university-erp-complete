package com.example.demo.student.controller;

import com.example.demo.student.service.HandoutService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/handout")
public class HandoutController {

    private final HandoutService handoutService;

    public HandoutController(HandoutService handoutService) {
        this.handoutService = handoutService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String, List<String>> body) {
        List<String> studentIds    = body.get("studentIds");
        List<String> documentTypes = body.get("documentTypes");

        if (studentIds == null || studentIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Select at least one student."));
        }
        if (documentTypes == null || documentTypes.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Select at least one document type."));
        }
        try {
            byte[] zip = handoutService.generateHandoutZip(studentIds, documentTypes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/zip"));
            headers.setContentDispositionFormData("attachment", "certificates.zip");
            headers.setContentLength(zip.length);
            return ResponseEntity.ok().headers(headers).body(zip);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to generate: " + e.getMessage()));
        }
    }
}
