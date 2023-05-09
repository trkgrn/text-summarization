package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.dto.SimilarityDto;
import com.trkgrn.springbackend.model.entity.Document;
import com.trkgrn.springbackend.model.entity.IncludeOn;
import com.trkgrn.springbackend.model.entity.Sentence;
import com.trkgrn.springbackend.model.entity.Similarity;
import com.trkgrn.springbackend.repository.DocumentRepository;
import com.trkgrn.springbackend.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final SentenceService sentenceService;
    private final DocumentConverter converter;
    private final StringUtil stringUtil;


    public DocumentDto getDocumentDtoById(Long id) {
        Optional<Document> documentOptional = Optional.ofNullable(documentRepository.getDocumentByDocumentId(id));
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            DocumentDto documentDto = converter.documentToDto(document);
            return documentDto;
        }
        return null;
    }

    public Document getDocumentById(Long id) {
        Optional<Document> documentOptional = Optional.ofNullable(documentRepository.getDocumentByDocumentId(id));
        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            return document;
        }
        return null;
    }

    public DocumentDto createDocument(String document, String title) {
        List<String> sentences = stringUtil.splitSentences(document);
        Document documentEntity = new Document();
        documentEntity.setName(UUID.randomUUID().toString());
        documentEntity.setTitle(title);
        AtomicInteger sentenceNo = new AtomicInteger(0);
        List<IncludeOn> sentenceEntities = sentences.stream().map(sentence -> {
            Sentence sentenceEntity = new Sentence();
            sentenceEntity.setText(sentence);
            sentenceEntity.setSimilarities(List.of());
            sentenceEntity.setSentenceScore(0.0);
            sentenceEntity.setNumberOfEdgeExceedingThreshold(0);
            sentenceEntity.setIsIncludedSummary(false);
            IncludeOn includeOn = new IncludeOn();
            includeOn.setSentence(sentenceEntity);
            includeOn.setSentenceNo(sentenceNo.incrementAndGet());
            return includeOn;
        }).collect(Collectors.toList());
        documentEntity.setSentences(sentenceEntities);
        Document savedDocument = documentRepository.save(documentEntity);

        List<Sentence> sentencesList = savedDocument.getSentences().stream()
                .map(sentence -> sentence.getSentence())
                .collect(Collectors.toList());

        for (int i = 0; i < sentencesList.size(); i++) {
            List<Similarity> similarities = new ArrayList<>();
            for (int j = i + 1; j < sentencesList.size(); j++) {
                Similarity similarity = new Similarity();
                similarity.setSimilarityRate(0.0);
                similarity.setSentence(sentencesList.get(j));
                similarities.add(similarity);
            }
            if (similarities.size() > 0) {
                sentencesList.get(i).setSimilarities(similarities);
            }
        }
        sentenceService.saveAll(sentencesList);
        DocumentDto documentDto = getDocumentDtoById(savedDocument.getDocumentId());
        return documentDto;
    }





    public void saveSentencesByDocumentDto(DocumentDto documentDto) {

        Document document = getDocumentById(documentDto.getDocumentId());

        List<Sentence> sentencesList = document.getSentences().stream()
                .map(sentence -> sentence.getSentence())
                .collect(Collectors.toList());


        for (int i = 0; i < sentencesList.size(); i++) {
            List<Similarity> similarities = sentencesList.get(i).getSimilarities();
            for (int j = 0; j < similarities.size(); j++) {
                similarities.get(j).setSimilarityRate(documentDto.getSentences().get(i).getSimilarities().get(j).getSimilarityRate());
            }
            if (similarities.size() > 0) {
                sentencesList.get(i).setSimilarities(similarities);
            }
            sentencesList.get(i).setSentenceScore(documentDto.getSentences().get(i).getSentenceScore());
            sentencesList.get(i).setNumberOfEdgeExceedingThreshold(documentDto.getSentences().get(i).getNumberOfEdgeExceedingThreshold());
            sentencesList.get(i).setIsIncludedSummary(documentDto.getSentences().get(i).getIsIncludedSummary());
        }
        sentenceService.saveAll(sentencesList);

    }
}

