package com.trkgrn.springbackend.model.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
public class Sentence {

    @Id
    @GeneratedValue
    private Long id;
    private String text;


}
