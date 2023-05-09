package com.trkgrn.springbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sentence {

    public Sentence(Long sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Id
    @GeneratedValue
    private Long sentenceId;
    private String text;
    private Integer numberOfEdgeExceedingThreshold;
    private Double sentenceScore;
    private Boolean isIncludedSummary;

    @Relationship(type = "SIMILARITY", direction = Relationship.Direction.OUTGOING)
    private List<Similarity> similarities;


}
