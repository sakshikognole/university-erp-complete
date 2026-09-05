package com.example.demo.payment.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payment_titles")
public class PaymentTitle {

    @Id
    private String id;

    private String titleId;   // PT001, PT002 ...
    private String title;
    private double amount;
    private double discount;  // actual discount value (NOT final amount)

    public PaymentTitle() {}

    public String getId()                  { return id; }
    public void   setId(String v)          { this.id = v; }
    public String getTitleId()             { return titleId; }
    public void   setTitleId(String v)     { this.titleId = v; }
    public String getTitle()               { return title; }
    public void   setTitle(String v)       { this.title = v; }
    public double getAmount()              { return amount; }
    public void   setAmount(double v)      { this.amount = v; }
    public double getDiscount()            { return discount; }
    public void   setDiscount(double v)    { this.discount = v; }
}
