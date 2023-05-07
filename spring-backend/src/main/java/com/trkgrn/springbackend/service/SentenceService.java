package com.trkgrn.springbackend.service;

import com.trkgrn.springbackend.model.entity.Sentence;
import com.trkgrn.springbackend.repository.SentenceRepository;
import org.springframework.stereotype.Service;

@Service
public class SentenceService {
    private final SentenceRepository sentenceRepository;

    public SentenceService(SentenceRepository sentenceRepository) {
        this.sentenceRepository = sentenceRepository;
    }

    public void saveAll(Iterable<Sentence> sentences) {
        sentenceRepository.saveAll(sentences);
    }
}
