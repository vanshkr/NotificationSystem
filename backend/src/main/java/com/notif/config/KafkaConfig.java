package com.notif.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// @Configuration tells Spring this class declares beans.
// Beans are objects Spring manages — created once, shared across the app.
@Configuration
public class KafkaConfig {

    public static final String TOPIC_NAME = "notification-events";

    // Declares the Kafka topic as a Spring bean.
    // If the topic already exists, Spring skips creation — safe to run repeatedly.
    @Bean
    public NewTopic notificationEventsTopic() {
        return TopicBuilder.name(TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
