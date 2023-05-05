package com.trkgrn.springbackend.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {


    @KafkaListener(topics = "${kafka.topic.name}", groupId = "group_id")
    public void consume(String message)   {
        System.out.println("Consumed message: " + message);
    }

}
