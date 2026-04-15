package com.notif.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is the Kafka message payload — what gets serialized to JSON
// and sent across the topic. Plain POJO, no JPA annotations.
// Lombok generates constructor, getters, setters for us.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    // UUID — used as the Redis dedup key later
    private String eventId;

    // ORDER_PLACED, USER_SIGNUP, PAYMENT_FAILED, SYSTEM_ALERT, REMINDER
    private String eventType;

    // Email address or phone number depending on channel
    private String recipient;

    // EMAIL or SMS
    private String channel;

    // Any extra info — e.g. order number, alert message
    private String payload;

}
