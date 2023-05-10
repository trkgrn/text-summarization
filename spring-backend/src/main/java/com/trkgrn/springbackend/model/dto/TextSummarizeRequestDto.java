package com.trkgrn.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TextSummarizeRequestDto {

    private String uuid;
    private String text;
    private String title;
    private String referenceSummary;
    private Double similarityThreshold;
    private Double scoreThreshold;

}
