package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LanguageProcessService {
    private final SentenceService sentenceService;
    private final DocumentService documentService;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final DocumentConverter converter;

    private static final String SIMILARITIES_API = "http://localhost:5000/api/v1/language-process/document/similarities";
    private static final String SENTENCE_SCORES_API = "http://localhost:5000/api/v1/language-process/document/sentence-scores";

    public void textSummarization(String text, String title, Double threshold) {
        DocumentDto document = documentService.createDocument(text, title); // STAGE 1
        DocumentDto processedDocument = getSimilaritiesByDocument(document); // STAGE 2
        documentService.saveSentencesByDocumentDto(processedDocument, threshold);
        processedDocument = getSentenceScoresByDocument(processedDocument); // STAGE 3
        documentService.saveSentencesByDocumentDto(processedDocument, threshold);

    }


    public DocumentDto getSimilaritiesByDocument(DocumentDto document) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<DocumentDto> request = new HttpEntity<>(document, httpHeaders);
        ResponseEntity<DocumentDto> response = restTemplate.postForEntity(SIMILARITIES_API, request, DocumentDto.class);
        return response.getBody();
    }

    public DocumentDto getSentenceScoresByDocument(DocumentDto document) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<DocumentDto> request = new HttpEntity<>(document, httpHeaders);
        ResponseEntity<DocumentDto> response = restTemplate.postForEntity(SENTENCE_SCORES_API, request, DocumentDto.class);
        return response.getBody();
    }


}
