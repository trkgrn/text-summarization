package com.trkgrn.springbackend.repository;

import com.trkgrn.springbackend.model.entity.Sentence;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SentenceRepository extends Neo4jRepository<Sentence, Long> {
}
