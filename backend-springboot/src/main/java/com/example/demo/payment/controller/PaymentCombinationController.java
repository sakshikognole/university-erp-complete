package com.example.demo.payment.controller;

import com.example.demo.payment.service.PaymentCombinationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment-combinations")
public class PaymentCombinationController {

    private final PaymentCombinationService service;

    public PaymentCombinationController(PaymentCombinationService service) {
        this.service = service;
    }

    // GET /api/payment-combinations
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // POST /api/payment-combinations
    // Body: { "titleIds": ["PT001", "PT002"] }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, List<String>> body) {
        List<String> titleIds = body.get("titleIds");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(titleIds));
        } catch (IllegalStateException ex) {
            // Duplicate combination
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
