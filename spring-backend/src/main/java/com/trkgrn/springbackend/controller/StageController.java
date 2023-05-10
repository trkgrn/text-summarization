package com.trkgrn.springbackend.controller;

import com.trkgrn.springbackend.model.dto.MessageDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StageController {

    private final SimpMessagingTemplate messagingTemplate;

    public StageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/stage/{documentId}")
    public void convertAndSend(@DestinationVariable(value = "documentId") String documentID,@Payload MessageDto message) {
        System.out.println("handledDoc: " + documentID + " stage:" + message);
        messagingTemplate.convertAndSend("/topic/documents/" + documentID, message);
    }


}
