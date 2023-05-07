package com.trkgrn.springbackend.model.entity;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
@Data
public class Document {

    @Id
    @GeneratedValue
    private Long documentId;
    private String name;
    @Relationship(type = "INCLUDE_ON", direction = Relationship.Direction.OUTGOING)
    private List<IncludeOn> sentences;

}
