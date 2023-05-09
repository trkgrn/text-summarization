package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.dto.SimilarityDto;
import com.trkgrn.springbackend.model.dto.SummarizedDocumentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LanguageProcessService {

    private final DocumentService documentService;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    private static final String SIMILARITIES_API = "http://localhost:5000/api/v1/language-process/document/similarities";
    private static final String SENTENCE_SCORES_API = "http://localhost:5000/api/v1/language-process/document/sentence-scores";
    private static final String ROUGE_SCORE_API = "http://localhost:5000/api/v1/language-process/document/rouge-score";

    public SummarizedDocumentDto summarizeText(String text, String title, Double similarityThreshold, Double scoreThreshold, String referenceSummary) {
        DocumentDto document = documentService.createDocument(text, title); // STAGE 1
        DocumentDto processedDocument = getSimilaritiesByDocument(document); // STAGE 2
        processedDocument = calculateEdgeCount(processedDocument, similarityThreshold);
        documentService.saveSentencesByDocumentDto(processedDocument);
        processedDocument = getSentenceScoresByDocument(processedDocument); // STAGE 3
        documentService.saveSentencesByDocumentDto(processedDocument);
        processedDocument = summarizedDocumentDto(processedDocument, scoreThreshold); // STAGE 4
        documentService.saveSentencesByDocumentDto(processedDocument);
        String summary = getSummarizedText(processedDocument);
        SummarizedDocumentDto summarizedDocument = new SummarizedDocumentDto(summary, referenceSummary, 0.0, processedDocument);
        summarizedDocument = calculateRougeScore(summarizedDocument); // STAGE 5

        return summarizedDocument;
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

    private String getSummarizedText(DocumentDto documentDto) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < documentDto.getSentences().size(); i++) {
            if (documentDto.getSentences().get(i).getIsIncludedSummary()) {
                sb.append(documentDto.getSentences().get(i).getText());
                sb.append(" ");
            }
        }
        return sb.toString();
    }


    public SummarizedDocumentDto calculateRougeScore(SummarizedDocumentDto summarizedDocumentDto) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<SummarizedDocumentDto> request = new HttpEntity<>(summarizedDocumentDto, httpHeaders);
        ResponseEntity<SummarizedDocumentDto> response = restTemplate.postForEntity(ROUGE_SCORE_API, request, SummarizedDocumentDto.class);
        return response.getBody();
    }


}
