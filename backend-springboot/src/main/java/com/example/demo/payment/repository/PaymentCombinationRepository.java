package com.example.demo.payment.repository;

import com.example.demo.payment.model.PaymentCombination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCombinationRepository
        extends MongoRepository<PaymentCombination, String> {

    // Find by the sorted list of titles — used for duplicate check
    Optional<PaymentCombination> findByPaymentTitles(List<String> paymentTitles);

    // Latest paymentId for auto-increment  (e.g. "PAY005")
    PaymentCombination findTopByOrderByPaymentIdDesc();
}
