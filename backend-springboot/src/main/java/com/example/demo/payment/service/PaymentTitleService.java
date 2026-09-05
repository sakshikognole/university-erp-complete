package com.example.demo.payment.service;

import com.example.demo.payment.model.PaymentTitle;
import com.example.demo.payment.repository.PaymentTitleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentTitleService {

    private final PaymentTitleRepository repo;

    public PaymentTitleService(PaymentTitleRepository repo) {
        this.repo = repo;
    }

    public Page<PaymentTitle> getAll(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    public PaymentTitle create(PaymentTitle title) {
        if (title.getTitle() == null || title.getTitle().isBlank())
            throw new IllegalArgumentException("Title is required.");
        if (title.getAmount() <= 0)
            throw new IllegalArgumentException("Amount must be greater than 0.");
        // Auto-generate titleId
        long count = repo.count();
        title.setTitleId(String.format("PT%03d", count + 1));
        return repo.save(title);
    }
}
