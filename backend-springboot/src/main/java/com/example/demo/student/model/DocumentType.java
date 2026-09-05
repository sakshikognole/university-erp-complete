package com.example.demo.student.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "document_types")
public class DocumentType {

    @Id
    private String id;

    @Indexed(unique = true)
    private String documentName;

    private String defaultContent;

    public DocumentType() {}

    public DocumentType(String documentName, String defaultContent) {
        this.documentName   = documentName;
        this.defaultContent = defaultContent;
    }

    public String getId()                         { return id; }
    public void   setId(String id)                { this.id = id; }
    public String getDocumentName()               { return documentName; }
    public void   setDocumentName(String n)       { this.documentName = n; }
    public String getDefaultContent()             { return defaultContent; }
    public void   setDefaultContent(String c)     { this.defaultContent = c; }
}
