package com.example.demo.materials.controller;

import com.example.demo.materials.model.MaterialFolder;
import com.example.demo.materials.service.MaterialFolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/material-folders")
public class MaterialFolderController {

    private final MaterialFolderService folderService;

    public MaterialFolderController(MaterialFolderService folderService) {
        this.folderService = folderService;
    }

    /**
     * GET /api/material-folders
     *
     * Teacher (X-User-Role: TEACHER):
     *   Returns folders owned by X-Teacher-Id.
     *   If ?parentFolderId=xxx → sub-folders of that parent.
     *   If ?parentFolderId omitted → root folders.
     *   If ?all=true → all folders flat (folder picker).
     *
     * Student / other roles:
     *   Returns all folders (read-only).
     */
    @GetMapping
    public ResponseEntity<List<MaterialFolder>> getFolders(
            @RequestHeader(value = "X-User-Role",   defaultValue = "STUDENT") String role,
            @RequestHeader(value = "X-Teacher-Id",  defaultValue = "")        String teacherId,
            @RequestParam(value = "parentFolderId", required = false)          String parentFolderId,
            @RequestParam(value = "all",            defaultValue = "false")    boolean all) {

        if ("TEACHER".equalsIgnoreCase(role)) {
            if (all) {
                return ResponseEntity.ok(folderService.getAllFolders(teacherId));
            }
            if (parentFolderId != null && !parentFolderId.isBlank()) {
                return ResponseEntity.ok(
                        folderService.getSubFolders(teacherId, parentFolderId));
            }
            return ResponseEntity.ok(folderService.getRootFolders(teacherId));
        }
        // Students see all folders
        return ResponseEntity.ok(folderService.getAllFolders());
    }

    // GET /api/material-folders/{folderId}
    @GetMapping("/{folderId}")
    public ResponseEntity<?> getOne(@PathVariable String folderId) {
        try {
            return ResponseEntity.ok(folderService.getByFolderId(folderId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/material-folders  — TEACHER only
    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "X-User-Role",  defaultValue = "") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "") String teacherId,
            @RequestBody Map<String, String> body) {

        if (!"TEACHER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only teachers can create folders."));
        }
        try {
            MaterialFolder folder = folderService.create(
                    body.get("folderName"),
                    teacherId,
                    body.get("parentFolderId"));
            return ResponseEntity.status(HttpStatus.CREATED).body(folder);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // PUT /api/material-folders/{folderId}  — TEACHER only, owns folder
    @PutMapping("/{folderId}")
    public ResponseEntity<?> rename(
            @PathVariable String folderId,
            @RequestHeader(value = "X-User-Role",  defaultValue = "") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "") String teacherId,
            @RequestBody Map<String, String> body) {

        if (!"TEACHER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only teachers can rename folders."));
        }
        try {
            return ResponseEntity.ok(
                    folderService.rename(folderId, body.get("folderName"), teacherId));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/material-folders/{folderId}  — TEACHER only, owns folder
    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> delete(
            @PathVariable String folderId,
            @RequestHeader(value = "X-User-Role",  defaultValue = "") String role,
            @RequestHeader(value = "X-Teacher-Id", defaultValue = "") String teacherId) {

        if (!"TEACHER".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only teachers can delete folders."));
        }
        try {
            folderService.delete(folderId, teacherId);
            return ResponseEntity.ok(
                    Map.of("message", "Folder deleted successfully."));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
