package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.dto.SimilarityDto;
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

    public void textSummarization(String text, String title, Double similarityThreshold, Double scoreThreshold) {
        DocumentDto document = documentService.createDocument(text, title); // STAGE 1
        DocumentDto processedDocument = getSimilaritiesByDocument(document); // STAGE 2
        processedDocument = calculateEdgeCount(processedDocument, similarityThreshold);
        documentService.saveSentencesByDocumentDto(processedDocument);
        processedDocument = getSentenceScoresByDocument(processedDocument); // STAGE 3
        documentService.saveSentencesByDocumentDto(processedDocument);
        processedDocument = summarizedDocumentDto(processedDocument, scoreThreshold); // STAGE 4
        documentService.saveSentencesByDocumentDto(processedDocument);

    }


    public DocumentDto getSimilaritiesByDocument(DocumentDto document) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<DocumentDto> request = new HttpEntity<>(document, httpHeaders);
        ResponseEntity<DocumentDto> response = restTemplate.postForEntity(SIMILARITIES_API, request, DocumentDto.class);
        return response.getBody();
    }


    public DocumentDto calculateEdgeCount(DocumentDto documentDto, Double similarityThreshold) {
        for (int i = 0; i < documentDto.getSentences().size(); i++) {
            documentDto.getSentences().get(i).setNumberOfEdgeExceedingThreshold(0);
        }

        for (int i = 0; i < documentDto.getSentences().size(); i++) {

            for (SimilarityDto similarity : documentDto.getSentences().get(i).getSimilarities()) {
                if (similarity.getSimilarityRate() > similarityThreshold) {
                    Long sentenceId = similarity.getSentence().getSentenceId();
                    documentDto.getSentences().get(i).setNumberOfEdgeExceedingThreshold(documentDto.getSentences().get(i).getNumberOfEdgeExceedingThreshold() + 1);
                    for (int j = i + 1; j < documentDto.getSentences().size(); j++) {
                        if (documentDto.getSentences().get(j).getSentenceId().equals(sentenceId)) {
                            documentDto.getSentences().get(j).setNumberOfEdgeExceedingThreshold(documentDto.getSentences().get(j).getNumberOfEdgeExceedingThreshold() + 1);
                            break;
                        }
                    }
                }
            }

        }
        return documentDto;
    }



    public DocumentDto getSentenceScoresByDocument(DocumentDto document) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<DocumentDto> request = new HttpEntity<>(document, httpHeaders);
        ResponseEntity<DocumentDto> response = restTemplate.postForEntity(SENTENCE_SCORES_API, request, DocumentDto.class);
        return response.getBody();
    }

    public DocumentDto summarizedDocumentDto(DocumentDto documentDto, Double scoreThreshold) {
        for (int i = 0; i < documentDto.getSentences().size(); i++) {
            Double score = documentDto.getSentences().get(i).getSentenceScore();
            documentDto.getSentences().get(i).setIsIncludedSummary(score > scoreThreshold);
        }

        return documentDto;
    }


}
