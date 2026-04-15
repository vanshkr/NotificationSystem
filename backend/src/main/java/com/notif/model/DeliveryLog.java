package com.notif.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

// @Entity tells JPA this class maps to a DB table.
// @Table(name) explicitly sets the table name — without it JPA
// would default to "delivery_log" anyway, but being explicit is good practice.
@Entity
@Table(name = "delivery_log")
@Data  // Lombok: generates getters, setters, equals, hashCode, toString
public class DeliveryLog {

    // BIGSERIAL in Postgres = auto-incrementing Long in Java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UNIQUE constraint — belt-and-suspenders dedup below Redis
    @Column(name = "event_id", unique = true, nullable = false, length = 100)
    private String eventId;

    @Column(nullable = false)
    private String recipient;

    // 'EMAIL' or 'SMS' — length 10 is enough
    @Column(nullable = false, length = 10)
    private String channel;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    // 'SENT', 'FAILED', 'DUPLICATE', 'EXHAUSTED'
    @Column(nullable = false, length = 20)
    private String status;

    // 'BREVO' or 'TWILIO' — null until a send is attempted
    @Column(length = 20)
    private String provider;

    // null on success, populated on failure
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // Auto-set at insert time — mirrors DEFAULT NOW() in SQL
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Populated only when status = SENT
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    // Sets createdAt automatically before first insert
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
