package com.trkgrn.springbackend.converter;

import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.dto.SentenceDto;
import com.trkgrn.springbackend.model.entity.Document;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DocumentConverter {


    public DocumentDto documentToDto(Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(document.getId());
        documentDto.setName(document.getName());
        documentDto.setSentences(document.getSentences()
                .stream()
                .map(sentence -> {
                    SentenceDto sentenceDto = new SentenceDto();
                    sentenceDto.setId(sentence.getId());
                    sentenceDto.setSentence(sentence.getSentence().getText());
                    sentenceDto.setSentenceNo(sentence.getSentenceNo());
                    return sentenceDto;
                }).collect(Collectors.toList()));
        return documentDto;
    }
}
