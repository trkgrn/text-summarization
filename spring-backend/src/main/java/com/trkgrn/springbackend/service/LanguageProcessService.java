package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.entity.Document;
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

    public void textSummarization(String text) {
        DocumentDto document = documentService.createDocument(text); // STAGE 1
        DocumentDto processedDocument = getSimilaritiesByDocument(document); // STAGE 2
        documentService.saveSimilarities(processedDocument); // STAGE 3

    }


    public DocumentDto getSimilaritiesByDocument(DocumentDto document) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<DocumentDto> request = new HttpEntity<>(document,httpHeaders);

        ResponseEntity<DocumentDto> response = restTemplate.postForEntity(SIMILARITIES_API, request, DocumentDto.class);
        return response.getBody();
    }


}
