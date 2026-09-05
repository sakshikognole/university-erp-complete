package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "success", true,
            "message", "University ERP Spring Boot API is running",
            "endpoints", Map.of(
                "books",      "/api/books",
                "clubs",      "/api/clubs",
                "sports",     "/api/sports",
                "students",   "/api/students",
                "documents",  "/api/document-types",
                "letterhead", "/api/letterhead"
            )
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
