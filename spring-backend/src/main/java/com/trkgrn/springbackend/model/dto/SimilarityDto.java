package com.trkgrn.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarityDto {
    private Long similarityId;
    private Double similarityRate;
    private TargetSentenceDto sentence;
}
