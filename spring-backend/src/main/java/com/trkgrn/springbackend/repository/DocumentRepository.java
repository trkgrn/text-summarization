package com.trkgrn.springbackend.repository;

import com.trkgrn.springbackend.model.entity.Document;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DocumentRepository extends Neo4jRepository<Document, Long> {

    Document getDocumentByDocumentId(Long documentId);
}
