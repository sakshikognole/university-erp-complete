package com.example.demo.payment.controller;

import com.example.demo.payment.model.PaymentTitle;
import com.example.demo.payment.service.PaymentTitleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment-titles")
public class PaymentTitleController {

    private final PaymentTitleService service;

    public PaymentTitleController(PaymentTitleService service) {
        this.service = service;
    }

    // GET /api/payment-titles?page=0&size=10
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(page, size));
    }

    // POST /api/payment-titles
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PaymentTitle title) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.create(title));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
