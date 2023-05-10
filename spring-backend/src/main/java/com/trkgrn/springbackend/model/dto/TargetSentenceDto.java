package com.trkgrn.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetSentenceDto {
    private Long sentenceId;
    private String text;
}
