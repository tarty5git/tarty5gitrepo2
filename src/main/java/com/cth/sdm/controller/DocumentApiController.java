package com.cth.sdm.controller;

import com.cth.sdm.entity.SDLCPhaseDocument;
import com.cth.sdm.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Document Management API", description = "Endpoints for uploading, picking up, and inspecting SDLC phase documents")
public class DocumentApiController {

    private final DocumentService documentService;

    public DocumentApiController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a new document (Excel, Word, PowerPoint, XML, etc.)")
    public ResponseEntity<Map<String, Object>> uploadDocument(
            @RequestParam("phaseNum") int phaseNum,
            @RequestParam("deliverableCode") String deliverableCode,
            @RequestParam("docTitle") String docTitle,
            @RequestParam(value = "docDescription", required = false) String docDescription,
            @RequestParam("docVersion") String docVersion,
            @RequestParam(value = "projectCode", required = false, defaultValue = "PRJ-01") String projectCode,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        try {
            String username = authentication != null ? authentication.getName() : "maker";
            SDLCPhaseDocument doc = documentService.submitDocument(
                    phaseNum, deliverableCode, docTitle, docDescription, docVersion, projectCode, file, username);

            response.put("status", "SUCCESS");
            response.put("message", "Document submitted successfully for Maker-Checker review");
            response.put("docId", doc.getDocId());
            response.put("projectCode", doc.getProjectCode());
            response.put("processed", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Failed to submit document: " + e.getMessage());
            response.put("processed", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    @Operation(summary = "List all documents with optional project level filter")
    public ResponseEntity<List<SDLCPhaseDocument>> getAllDocuments(
            @RequestParam(value = "projectFilter", required = false, defaultValue = "ALL") String projectFilter) {
        return ResponseEntity.ok(documentService.getAllDocuments(projectFilter));
    }

    @GetMapping("/{docId}")
    @Operation(summary = "Get document details by Document ID")
    public ResponseEntity<SDLCPhaseDocument> getDocumentById(@PathVariable String docId) {
        return documentService.getDocumentById(docId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{docId}/download")
    @Operation(summary = "Download stored document file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String docId) {
        return documentService.getDocumentById(docId)
                .map(doc -> {
                    if (doc.getFilePath() == null) {
                        return ResponseEntity.notFound().<Resource>build();
                    }
                    File file = new File(doc.getFilePath());
                    if (!file.exists()) {
                        return ResponseEntity.notFound().<Resource>build();
                    }
                    Resource resource = new FileSystemResource(file);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getOriginalFilename() + "\"")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
