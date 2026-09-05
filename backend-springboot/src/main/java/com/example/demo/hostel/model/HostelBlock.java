package com.example.demo.hostel.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hostel_blocks")
public class HostelBlock {

    @Id
    private String id;

    @Indexed(unique = true)
    private String blockId;       // HB001, HB002 ...

    private String hostelName;    // e.g. "Shivaji Block"
    private String type;          // BOYS or GIRLS
    private boolean active;       // checkbox — is hostel active

    public HostelBlock() {}

    public String  getId()                   { return id; }
    public void    setId(String v)           { this.id = v; }
    public String  getBlockId()              { return blockId; }
    public void    setBlockId(String v)      { this.blockId = v; }
    public String  getHostelName()           { return hostelName; }
    public void    setHostelName(String v)   { this.hostelName = v; }
    public String  getType()                 { return type; }
    public void    setType(String v)         { this.type = v; }
    public boolean isActive()                { return active; }
    public void    setActive(boolean v)      { this.active = v; }
}
