package com.trkgrn.springbackend.converter;

import com.trkgrn.springbackend.model.dto.DocumentDto;
import com.trkgrn.springbackend.model.dto.SentenceDto;
import com.trkgrn.springbackend.model.dto.SimilarityDto;
import com.trkgrn.springbackend.model.dto.TargetSentenceDto;
import com.trkgrn.springbackend.model.entity.Document;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DocumentConverter {


    public DocumentDto documentToDto(Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setDocumentId(document.getDocumentId());
        documentDto.setName(document.getName());
        documentDto.setSentences(document.getSentences()
                .stream()
                .map(sentence -> {
                    SentenceDto sentenceDto = new SentenceDto();
                    sentenceDto.setIncludeOnId(sentence.getIncludeOnId());
                    sentenceDto.setSentenceId(sentence.getSentence().getSentenceId());
                    sentenceDto.setText(sentence.getSentence().getText());
                    sentenceDto.setSentenceNo(sentence.getSentenceNo());
                    sentenceDto.setSimilarities(sentence.getSentence().getSimilarities()
                            .stream()
                            .map(similarity -> {
                                SimilarityDto similarityDto = new SimilarityDto();
                                similarityDto.setSimilarityId(similarity.getSimilarityId());
                                similarityDto.setSimilarityRate(similarity.getSimilarityRate());
                                similarityDto.setSentence(new TargetSentenceDto(similarity.getSentence().getSentenceId(), similarity.getSentence().getText()));
                                return similarityDto;
                            }).collect(Collectors.toList()));
                    return sentenceDto;
                }).collect(Collectors.toList()));
        return documentDto;
    }

}
