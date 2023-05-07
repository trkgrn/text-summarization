package com.trkgrn.springbackend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class StageController {

    private final SimpMessagingTemplate messagingTemplate;

    public StageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/stage/{documentId}")
    public void convertAndSend(@DestinationVariable(value = "documentId") Long documentID, String stage) {
        System.out.println("handledDoc: " + documentID + " stage:" + stage);
        messagingTemplate.convertAndSend("/topic/documents/" + documentID, stage);
    }


}
