package com.notif.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notif.config.KafkaConfig;
import com.notif.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

// @Slf4j generates a logger field — use log.info(), log.error() etc.
// @RequiredArgsConstructor generates a constructor for all final fields —
// Spring uses this for dependency injection automatically.
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(NotificationEvent event) {
        try {
            // Serialize the event object to a JSON string
            String message = objectMapper.writeValueAsString(event);

            // Send to the Kafka topic — eventId is the message key.
            // Using a key ensures all events with the same eventId
            // go to the same partition (ordering guarantee).
            kafkaTemplate.send(KafkaConfig.TOPIC_NAME, event.getEventId(), message);

            log.info("Event sent to Kafka: eventId={}, type={}",
                    event.getEventId(), event.getEventType());

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", event.getEventId(), e);
        }
    }

}
