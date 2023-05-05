package com.trkgrn.springbackend.model.dto;

import lombok.Data;

@Data
public class SentenceDto {
    private Long id;
    private Integer sentenceNo;
    private String sentence;
}
