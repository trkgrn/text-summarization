package com.trkgrn.springbackend.model.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Data
public class IncludeOn {
    @Id
    @GeneratedValue
    private Long id;
    @Property("sentence_no")
    private Integer sentenceNo;

    @TargetNode
    private Sentence sentence;

}
