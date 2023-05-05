package com.trkgrn.springbackend.controller;

import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@RequestParam Long id) {
        DocumentDto documentDto = documentService.getDocumentById(id);
        return ResponseEntity.ok(documentDto);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody String document) {
        DocumentDto createdDocumentDto = documentService.createDocument(document);
        return ResponseEntity.ok(createdDocumentDto);
    }


}
