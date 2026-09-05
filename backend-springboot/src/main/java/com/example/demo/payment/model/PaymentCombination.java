package com.example.demo.payment.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "payment_combinations")
public class PaymentCombination {

    @Id
    private String id;

    private String            paymentId;      // PAY001, PAY002 ...
    private List<String>      paymentTitles;  // sorted list of title names
    private double            totalAmount;    // sum of (amount - discount) for each title
    private LocalDateTime     createdAt;

    public PaymentCombination() {}

    public String getId()                          { return id; }
    public void   setId(String v)                  { this.id = v; }
    public String getPaymentId()                   { return paymentId; }
    public void   setPaymentId(String v)           { this.paymentId = v; }
    public List<String> getPaymentTitles()         { return paymentTitles; }
    public void   setPaymentTitles(List<String> v) { this.paymentTitles = v; }
    public double getTotalAmount()                 { return totalAmount; }
    public void   setTotalAmount(double v)         { this.totalAmount = v; }
    public LocalDateTime getCreatedAt()            { return createdAt; }
    public void   setCreatedAt(LocalDateTime v)    { this.createdAt = v; }
}
