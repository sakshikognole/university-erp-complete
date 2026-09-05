package com.example.demo.payment;

import com.example.demo.payment.model.PaymentTitle;
import com.example.demo.payment.repository.PaymentTitleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentDataSeeder implements CommandLineRunner {

    private final PaymentTitleRepository repo;

    public PaymentDataSeeder(PaymentTitleRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return; // already seeded

        repo.saveAll(List.of(
            build("PT001", "Exam Fee",    2000, 100),
            build("PT002", "Library Fee", 1000,  50),
            build("PT003", "Sports Fee",   500,   0),
            build("PT004", "Bus Fee",      3000, 200),
            build("PT005", "Lab Fee",      1500,  75),
            build("PT006", "Hostel Fee",   5000, 500)
        ));

        System.out.println("[PaymentDataSeeder] Seeded 6 payment titles.");
    }

    private PaymentTitle build(String id, String title,
                               double amount, double discount) {
        PaymentTitle p = new PaymentTitle();
        p.setTitleId(id);
        p.setTitle(title);
        p.setAmount(amount);
        p.setDiscount(discount);
        return p;
    }
}
