package com.notif.controller;

import com.notif.kafka.NotificationProducer;
import com.notif.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
// Every method returns JSON automatically — no need to manually serialize.
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final NotificationProducer producer;

    @PostMapping
    public ResponseEntity<String> publishEvent(@RequestBody NotificationEvent event) {
        producer.send(event);
        return ResponseEntity.ok("Event published: " + event.getEventId());
    }

}
