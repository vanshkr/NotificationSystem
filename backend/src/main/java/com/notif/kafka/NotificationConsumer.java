package com.notif.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notif.model.DeliveryLog;
import com.notif.model.NotificationEvent;
import com.notif.repository.DeliveryLogRepository;
import com.notif.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final DeliveryLogRepository deliveryLogRepository;

    // groupId ensures each message is processed by only one instance
    // even when scaled to multiple pods
    @KafkaListener(topics = "notification-events", groupId = "notif-group")
    public void consume(String message) {
        try {
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);
            log.info("Event received: eventId={}, type={}, recipient={}",
                    event.getEventId(), event.getEventType(), event.getRecipient());

            // DB-level duplicate guard — catches replays before Redis dedup (added Week 2 Day 4-5)
            if (deliveryLogRepository.findByEventId(event.getEventId()).isPresent()) {
                log.warn("Duplicate event skipped: eventId={}", event.getEventId());
                return;
            }

            if ("EMAIL".equalsIgnoreCase(event.getChannel())) {
                handleEmail(event);
            } else {
                // SMS routing added in Week 2 Day 3
                log.warn("Unsupported channel: {} — eventId={}", event.getChannel(), event.getEventId());
            }

        } catch (Exception e) {
            log.error("Failed to process message: {}", message, e);
        }
    }

    private void handleEmail(NotificationEvent event) {
        try {
            emailService.send(event);
            deliveryLogRepository.save(buildLog(event, "SENT", "BREVO", null, LocalDateTime.now()));
        } catch (RestClientException e) {
            log.error("Brevo send failed: eventId={}, error={}", event.getEventId(), e.getMessage());
            deliveryLogRepository.save(buildLog(event, "FAILED", "BREVO", e.getMessage(), null));
        }
    }

    private DeliveryLog buildLog(NotificationEvent event, String status,
                                 String provider, String errorMessage, LocalDateTime sentAt) {
        DeliveryLog log = new DeliveryLog();
        log.setEventId(event.getEventId());
        log.setRecipient(event.getRecipient());
        log.setChannel(event.getChannel());
        log.setEventType(event.getEventType());
        log.setStatus(status);
        log.setProvider(provider);
        log.setErrorMessage(errorMessage);
        log.setSentAt(sentAt);
        return log;
    }
}