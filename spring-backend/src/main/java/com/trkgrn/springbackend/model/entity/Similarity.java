package com.trkgrn.springbackend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Similarity {
    @Id
    @GeneratedValue
    private Long similarityId;

    @Property("similarity_rate")
    private Double similarityRate;

    @TargetNode
    private Sentence sentence;
}
