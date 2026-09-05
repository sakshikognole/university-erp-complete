package com.example.demo.payment.repository;

import com.example.demo.payment.model.PaymentTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTitleRepository extends MongoRepository<PaymentTitle, String> {
    boolean existsByTitleId(String titleId);
}
