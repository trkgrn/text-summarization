package com.trkgrn.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummarizedDocumentDto {
    private String summary;
    private String reference;
    private Double rougeScore;
    private DocumentDto document;
}
