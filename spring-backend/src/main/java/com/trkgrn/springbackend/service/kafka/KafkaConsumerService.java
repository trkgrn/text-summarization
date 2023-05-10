package com.trkgrn.springbackend.service.kafka;

import com.trkgrn.springbackend.model.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    @KafkaListener(topics = "${kafka.topic.name}", groupId = "group_id")
    public void consume(String message ) {
        System.out.println("Consumed message: " + message.replace("\"", ""));
        message = message.replace("\"", "");
        String[] messageParts = message.split(",");
        String name = messageParts[0].replace("name=", "");
        String stage = messageParts[1].replace("stage=", "");
        System.out.println("name: " + name + " stage: " + stage);
        messagingTemplate.convertAndSend("/topic/documents/" + name, new MessageDto(name, stage));
    }

}
