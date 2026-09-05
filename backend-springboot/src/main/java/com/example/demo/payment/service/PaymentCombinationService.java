package com.example.demo.payment.service;

import com.example.demo.payment.model.PaymentCombination;
import com.example.demo.payment.model.PaymentTitle;
import com.example.demo.payment.repository.PaymentCombinationRepository;
import com.example.demo.payment.repository.PaymentTitleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentCombinationService {

    private final PaymentCombinationRepository combinationRepo;
    private final PaymentTitleRepository       titleRepo;

    public PaymentCombinationService(PaymentCombinationRepository combinationRepo,
                                     PaymentTitleRepository titleRepo) {
        this.combinationRepo = combinationRepo;
        this.titleRepo       = titleRepo;
    }

    // ── Get all combinations ──────────────────────────────────────────────

    public List<PaymentCombination> getAll() {
        return combinationRepo.findAll();
    }

    // ── Create combination ────────────────────────────────────────────────

    /**
     * @param selectedTitleIds  list of titleId strings selected by admin
     * @return created PaymentCombination or throws if duplicate
     */
    public PaymentCombination create(List<String> selectedTitleIds) {
        if (selectedTitleIds == null || selectedTitleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one payment title is required.");
        }

        // Fetch all selected titles from DB
        List<PaymentTitle> titles = titleRepo.findAll().stream()
                .filter(t -> selectedTitleIds.contains(t.getTitleId()))
                .collect(Collectors.toList());

        if (titles.isEmpty()) {
            throw new IllegalArgumentException("No valid payment titles found.");
        }

        // Sorted title names — used for duplicate check (order-independent)
        List<String> sortedNames = titles.stream()
                .map(PaymentTitle::getTitle)
                .sorted()
                .collect(Collectors.toList());

        // Duplicate check on backend
        if (combinationRepo.findByPaymentTitles(sortedNames).isPresent()) {
            throw new IllegalStateException("This payment combination already exists.");
        }

        // Calculate total = sum of (amount - discount) for each title
        double total = titles.stream()
                .mapToDouble(t -> t.getAmount() - t.getDiscount())
                .sum();

        // Generate next Payment ID
        String paymentId = generateNextPaymentId();

        PaymentCombination combination = new PaymentCombination();
        combination.setPaymentId(paymentId);
        combination.setPaymentTitles(sortedNames);
        combination.setTotalAmount(total);
        combination.setCreatedAt(LocalDateTime.now());

        return combinationRepo.save(combination);
    }

    // ── Auto-increment Payment ID ─────────────────────────────────────────

    private String generateNextPaymentId() {
        PaymentCombination last = combinationRepo.findTopByOrderByPaymentIdDesc();
        if (last == null) return "PAY001";
        String lastId = last.getPaymentId(); // e.g. "PAY007"
        int num = Integer.parseInt(lastId.replace("PAY", ""));
        return String.format("PAY%03d", num + 1);
    }
}
