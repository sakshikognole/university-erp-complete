package com.example.demo.materials.controller;

import com.example.demo.materials.model.Material;
import com.example.demo.materials.service.MaterialService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // ── GET /api/materials ────────────────────────────────────────────────────
    // Teacher: ?folderId=xxx   → files in folder
    //          ?search=xxx     → search own files
    //          (neither)       → all own files
    // Student: ?folderId=xxx   → files in folder
    //          ?search=xxx     → search all files
    //          (neither)       → all files
    @GetMapping
    public ResponseEntity<List<Material>> getAll(
            @RequestHeader(value = "X-User-Role",  defaultValue = "STUDENT") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "")        String teacherId,
            @RequestParam(value = "folderId", required = false) String folderId,
            @RequestParam(value = "search",   required = false) String search) {

        if ("TEACHER".equalsIgnoreCase(role)) {
            if (folderId != null && !folderId.isBlank()) {
                return ResponseEntity.ok(
                        materialService.getByFolder(teacherId, folderId));
            }
            if (search != null) {
                return ResponseEntity.ok(
                        materialService.searchForTeacher(teacherId, search));
            }
            return ResponseEntity.ok(materialService.getAllForTeacher(teacherId));
        }

        // Student
        if (folderId != null && !folderId.isBlank()) {
            return ResponseEntity.ok(
                    materialService.getByFolderForStudent(folderId));
        }
        return ResponseEntity.ok(materialService.searchAll(search));
    }

    // GET /api/materials/{materialId}
    @GetMapping("/{materialId}")
    public ResponseEntity<?> getOne(@PathVariable String materialId) {
        try {
            return ResponseEntity.ok(materialService.getMeta(materialId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/materials/upload  — TEACHER only, multipart/form-data
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestHeader(value = "X-User-Role",  defaultValue = "") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "") String teacherId,
            @RequestParam("file")     MultipartFile file,
            @RequestParam("folderId") String folderId) {

        if (!"TEACHER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only teachers can upload files."));
        }
        try {
            Material saved = materialService.upload(file, folderId, teacherId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to store file: " + ex.getMessage()));
        }
    }

    // PUT /api/materials/{materialId}  — TEACHER rename
    @PutMapping("/{materialId}")
    public ResponseEntity<?> rename(
            @PathVariable String materialId,
            @RequestHeader(value = "X-User-Role",  defaultValue = "") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "") String teacherId,
            @RequestBody Map<String, String> body) {

        if (!"TEACHER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only teachers can rename files."));
        }
        try {
            return ResponseEntity.ok(
                    materialService.rename(materialId, body.get("fileName"), teacherId));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/materials/{materialId}  — TEACHER only
    @DeleteMapping("/{materialId}")
    public ResponseEntity<?> delete(
            @PathVariable String materialId,
            @RequestHeader(value = "X-User-Role",  defaultValue = "") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "") String teacherId) {

        if (!"TEACHER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only teachers can delete files."));
        }
        try {
            materialService.delete(materialId, teacherId);
            return ResponseEntity.ok(Map.of("message", "File deleted successfully."));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete file: " + ex.getMessage()));
        }
    }

    // GET /api/materials/{materialId}/download  — all roles
    @GetMapping("/{materialId}/download")
    public ResponseEntity<?> download(@PathVariable String materialId) {
        try {
            Material meta = materialService.getMeta(materialId);
            Path     path = materialService.resolveFilePath(materialId);
            Resource resource;
            try {
                resource = new UrlResource(path.toUri());
            } catch (MalformedURLException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Could not resolve file path."));
            }
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "File not found on server."));
            }

            // For PDF/images: inline (preview in browser)
            // For others: attachment (force download)
            String contentDisposition;
            MediaType mediaType;
            String ext = meta.getFileType().toLowerCase();
            switch (ext) {
                case "pdf"  -> { mediaType = MediaType.APPLICATION_PDF;
                                 contentDisposition = "inline; filename=\"" + meta.getFileName() + "\""; }
                case "jpg", "jpeg" -> { mediaType = MediaType.IMAGE_JPEG;
                                        contentDisposition = "inline; filename=\"" + meta.getFileName() + "\""; }
                case "png"  -> { mediaType = MediaType.IMAGE_PNG;
                                 contentDisposition = "inline; filename=\"" + meta.getFileName() + "\""; }
                default     -> { mediaType = MediaType.APPLICATION_OCTET_STREAM;
                                 contentDisposition = "attachment; filename=\"" + meta.getFileName() + "\""; }
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
