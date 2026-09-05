package com.example.demo.hostel.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "hostel_rooms")
public class HostelRoom {

    @Id
    private String id;

    private String roomId;      // HR001, HR002 ...
    private String blockId;     // references HostelBlock.blockId
    private String roomNo;      // e.g. "101", "A-12"
    private int    capacity;

    private List<RoomStudent> students = new ArrayList<>();

    public HostelRoom() {}

    public String getId()                         { return id; }
    public void   setId(String v)                 { this.id = v; }
    public String getRoomId()                     { return roomId; }
    public void   setRoomId(String v)             { this.roomId = v; }
    public String getBlockId()                    { return blockId; }
    public void   setBlockId(String v)            { this.blockId = v; }
    public String getRoomNo()                     { return roomNo; }
    public void   setRoomNo(String v)             { this.roomNo = v; }
    public int    getCapacity()                   { return capacity; }
    public void   setCapacity(int v)              { this.capacity = v; }
    public List<RoomStudent> getStudents()        { return students; }
    public void   setStudents(List<RoomStudent> v){ this.students = v; }
}
