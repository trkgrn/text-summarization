package com.trkgrn.springbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DocumentDto {
    private Long id;
    private String name;
    private List<SentenceDto> sentences;
}
