package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.converter.DocumentConverter;
import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.entity.Document;
import com.trkgrn.springbackend.model.entity.IncludeOn;
import com.trkgrn.springbackend.model.entity.Sentence;
import com.trkgrn.springbackend.repository.DocumentRepository;
import com.trkgrn.springbackend.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentConverter converter;
    private final StringUtil stringUtil;

    public DocumentService(DocumentRepository documentRepository, DocumentConverter converter, StringUtil stringUtil) {
        this.documentRepository = documentRepository;
        this.converter = converter;
        this.stringUtil = stringUtil;
    }


    public DocumentDto getDocumentById(Long id) {
       Optional<Document> documentOptional = Optional.ofNullable(documentRepository.getDocumentById(id));
         if (documentOptional.isPresent()) {
             Document document = documentOptional.get();
              DocumentDto documentDto = converter.documentToDto(document);
              return documentDto;
            }
        return null;
    }

    public DocumentDto createDocument(String document){
        List<String> sentences = stringUtil.splitSentences(document);
        Document documentEntity = new Document();
        documentEntity.setName(UUID.randomUUID().toString());
        AtomicInteger sentenceNo = new AtomicInteger(0);
        List<IncludeOn> sentenceEntities = sentences.stream().map(sentence -> {
            Sentence sentenceEntity = new Sentence();
            sentenceEntity.setText(sentence);
            IncludeOn includeOn = new IncludeOn();
            includeOn.setSentence(sentenceEntity);
            includeOn.setSentenceNo(sentenceNo.incrementAndGet());
            return includeOn;
        }).collect(Collectors.toList());
        documentEntity.setSentences(sentenceEntities);
        Document savedDocument = documentRepository.save(documentEntity);
        DocumentDto documentDto = converter.documentToDto(savedDocument);
        return documentDto;
    }


}
