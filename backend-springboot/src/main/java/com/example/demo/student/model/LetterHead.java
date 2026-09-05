package com.example.demo.student.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "letterhead")
public class LetterHead {

    @Id
    private String id;

    // Single-document pattern — always stored with key="default"
    private String key = "default";

    private String trustName;
    private String collegeName;
    private String address;
    private String phone;
    private String tollFree;
    private String fax;
    private String website;
    private String email;
    private String logoText;   // short text shown in the logo box on the PDF

    public LetterHead() {}

    public LetterHead(String trustName, String collegeName, String address,
                      String phone, String tollFree, String fax,
                      String website, String email, String logoText) {
        this.trustName   = trustName;
        this.collegeName = collegeName;
        this.address     = address;
        this.phone       = phone;
        this.tollFree    = tollFree;
        this.fax         = fax;
        this.website     = website;
        this.email       = email;
        this.logoText    = logoText;
    }

    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }
    public String getKey()                         { return key; }
    public void   setKey(String key)               { this.key = key; }
    public String getTrustName()                   { return trustName; }
    public void   setTrustName(String v)           { this.trustName = v; }
    public String getCollegeName()                 { return collegeName; }
    public void   setCollegeName(String v)         { this.collegeName = v; }
    public String getAddress()                     { return address; }
    public void   setAddress(String v)             { this.address = v; }
    public String getPhone()                       { return phone; }
    public void   setPhone(String v)               { this.phone = v; }
    public String getTollFree()                    { return tollFree; }
    public void   setTollFree(String v)            { this.tollFree = v; }
    public String getFax()                         { return fax; }
    public void   setFax(String v)                 { this.fax = v; }
    public String getWebsite()                     { return website; }
    public void   setWebsite(String v)             { this.website = v; }
    public String getEmail()                       { return email; }
    public void   setEmail(String v)               { this.email = v; }
    public String getLogoText()                    { return logoText; }
    public void   setLogoText(String v)            { this.logoText = v; }
}
