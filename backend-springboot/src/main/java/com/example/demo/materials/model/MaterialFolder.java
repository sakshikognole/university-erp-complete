package com.example.demo.materials.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "material_folders")
public class MaterialFolder {

    @Id
    private String id;

    private String folderId;
    private String folderName;
    private String teacherId;

    // null means root level
    private String parentFolderId;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public MaterialFolder() {}

    public String getId()                          { return id; }
    public void   setId(String v)                  { this.id = v; }
    public String getFolderId()                    { return folderId; }
    public void   setFolderId(String v)            { this.folderId = v; }
    public String getFolderName()                  { return folderName; }
    public void   setFolderName(String v)          { this.folderName = v; }
    public String getTeacherId()                   { return teacherId; }
    public void   setTeacherId(String v)           { this.teacherId = v; }
    public String getParentFolderId()              { return parentFolderId; }
    public void   setParentFolderId(String v)      { this.parentFolderId = v; }
    public LocalDateTime getCreatedDate()          { return createdDate; }
    public void          setCreatedDate(LocalDateTime v) { this.createdDate = v; }
    public LocalDateTime getUpdatedDate()          { return updatedDate; }
    public void          setUpdatedDate(LocalDateTime v) { this.updatedDate = v; }
}
