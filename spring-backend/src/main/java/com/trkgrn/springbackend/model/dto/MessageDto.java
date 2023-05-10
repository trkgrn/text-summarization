package com.trkgrn.springbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageDto {
    private String sender;
    private String message;
}
