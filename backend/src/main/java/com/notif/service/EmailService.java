package com.notif.service;

import com.notif.model.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${brevo.sender-name}")
    private String senderName;

    private final RestClient restClient = RestClient.create();

    public void send(NotificationEvent event) {
        Map<String, Object> body = Map.of(
                "sender",      Map.of("name", senderName, "email", senderEmail),
                "to",          List.of(Map.of("email", event.getRecipient())),
                "subject",     subjectFor(event.getEventType()),
                "htmlContent", bodyFor(event)
        );

        restClient.post()
                .uri(BREVO_API_URL)
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        log.info("Email sent via Brevo: recipient={}, eventType={}",
                event.getRecipient(), event.getEventType());
    }

    private String subjectFor(String eventType) {
        return switch (eventType) {
            case "ORDER_PLACED"   -> "Your order has been placed";
            case "USER_SIGNUP"    -> "Welcome! Your account is ready";
            case "PAYMENT_FAILED" -> "Action required: Payment failed";
            case "SYSTEM_ALERT"   -> "System alert notification";
            case "REMINDER"       -> "You have a reminder";
            default               -> "Notification";
        };
    }

    private String bodyFor(NotificationEvent event) {
        return "<html><body>"
                + "<h2>" + subjectFor(event.getEventType()) + "</h2>"
                + "<p>Event ID: " + event.getEventId() + "</p>"
                + "<p>" + (event.getPayload() != null ? event.getPayload() : "") + "</p>"
                + "</body></html>";
    }
}