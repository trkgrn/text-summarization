package com.trkgrn.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentenceDto {
    private Long includeOnId;
    private Integer sentenceNo;
    private Long sentenceId;
    private String text;
    private Integer numberOfEdgeExceedingThreshold;
    private Double sentenceScore;
    private List<SimilarityDto> similarities;
}
