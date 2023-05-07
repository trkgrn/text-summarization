package com.trkgrn.springbackend.controller;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.entity.Document;
import com.trkgrn.springbackend.service.DocumentService;
import com.trkgrn.springbackend.service.LanguageProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final LanguageProcessService languageProcessService;
    private final DocumentConverter converter;



    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@RequestParam Long id) {
        DocumentDto documentDto = documentService.getDocumentDtoById(id);
        return ResponseEntity.ok(documentDto);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody String document) {
        DocumentDto createdDocumentDto = documentService.createDocument(document);
        return ResponseEntity.ok(createdDocumentDto);
    }

    @GetMapping("/{id}/similarities")
    public ResponseEntity<DocumentDto> getDocumentSimilarities(@PathVariable Long id) {
        DocumentDto documentDto = documentService.getDocumentDtoById(id);
        documentDto = languageProcessService.getSimilaritiesByDocument(documentDto);
        documentService.saveSimilarities(documentDto); // STAGE 3
        return ResponseEntity.ok(documentDto);
    }

}
