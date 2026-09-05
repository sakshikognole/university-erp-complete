package com.example.demo.materials.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "materials")
public class Material {

    @Id
    private String id;

    private String materialId;   // MAT-XXXXXXXX
    private String fileName;     // display name (can be renamed)
    private String storedFileName; // actual name on disk (never changes)
    private String fileType;     // PDF, JPG, JPEG, PNG, XLS, XLSX
    private long   fileSize;     // bytes
    private String teacherId;
    private String folderId;
    private String fileUrl;      // relative path: /api/materials/{id}/download
    private LocalDateTime uploadedDate;
    private LocalDateTime updatedDate;

    public Material() {}

    public String getId()                              { return id; }
    public void   setId(String v)                      { this.id = v; }
    public String getMaterialId()                      { return materialId; }
    public void   setMaterialId(String v)              { this.materialId = v; }
    public String getFileName()                        { return fileName; }
    public void   setFileName(String v)                { this.fileName = v; }
    public String getStoredFileName()                  { return storedFileName; }
    public void   setStoredFileName(String v)          { this.storedFileName = v; }
    public String getFileType()                        { return fileType; }
    public void   setFileType(String v)                { this.fileType = v; }
    public long   getFileSize()                        { return fileSize; }
    public void   setFileSize(long v)                  { this.fileSize = v; }
    public String getTeacherId()                       { return teacherId; }
    public void   setTeacherId(String v)               { this.teacherId = v; }
    public String getFolderId()                        { return folderId; }
    public void   setFolderId(String v)                { this.folderId = v; }
    public String getFileUrl()                         { return fileUrl; }
    public void   setFileUrl(String v)                 { this.fileUrl = v; }
    public LocalDateTime getUploadedDate()             { return uploadedDate; }
    public void          setUploadedDate(LocalDateTime v) { this.uploadedDate = v; }
    public LocalDateTime getUpdatedDate()              { return updatedDate; }
    public void          setUpdatedDate(LocalDateTime v)  { this.updatedDate = v; }
}
