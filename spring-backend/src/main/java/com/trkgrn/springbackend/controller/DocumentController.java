package com.trkgrn.springbackend.controller;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.dto.SummarizedDocumentDto;
import com.trkgrn.springbackend.model.dto.TextSummarizeRequestDto;
import com.trkgrn.springbackend.service.DocumentService;
import com.trkgrn.springbackend.service.LanguageProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document")
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

    @PostMapping("/create")
    public ResponseEntity<DocumentDto> createDocument(@RequestBody String document, @RequestParam String title) {
        DocumentDto createdDocumentDto = documentService.createDocument(UUID.randomUUID().toString(),document, title);
        return ResponseEntity.ok(createdDocumentDto);
    }

    @GetMapping("/{id}/score-threshold/{scoreThreshold}/similarity-threshold/{similarityThreshold}/similarities")
    public ResponseEntity<DocumentDto> getDocumentSimilaritiesByThreshold(@PathVariable Long id,
                                                                          @PathVariable Double similarityThreshold,
                                                                          @PathVariable Double scoreThreshold) {
        DocumentDto documentDto = documentService.getDocumentDtoById(id);
        documentDto = languageProcessService.getSimilaritiesByDocument(documentDto);
        documentDto = languageProcessService.calculateEdgeCount(documentDto, similarityThreshold);
        documentDto = languageProcessService.getSentenceScoresByDocument(documentDto);
        documentDto = languageProcessService.summarizedDocumentDto(documentDto, scoreThreshold);
        documentService.saveSentencesByDocumentDto(documentDto);
        return ResponseEntity.ok(documentDto);
    }

    @PostMapping("/text-summarize")
    public ResponseEntity<SummarizedDocumentDto> summarizeText(@RequestBody TextSummarizeRequestDto textSummarizeRequest) {
        SummarizedDocumentDto summarizedDocumentDto = languageProcessService.summarizeText(textSummarizeRequest.getUuid(),textSummarizeRequest.getText(),
                textSummarizeRequest.getTitle(),
                textSummarizeRequest.getSimilarityThreshold(),
                textSummarizeRequest.getScoreThreshold(),
                textSummarizeRequest.getReferenceSummary());
        return ResponseEntity.ok(summarizedDocumentDto);
    }


}
