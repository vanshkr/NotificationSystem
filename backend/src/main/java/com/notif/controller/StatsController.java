package com.notif.controller;

import com.notif.model.DeliveryLog;
import com.notif.repository.DeliveryLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatsController {

    private final DeliveryLogRepository repo;

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        var todayStart = LocalDate.now().atStartOfDay();
        return Map.of(
                "totalSent",      repo.countByStatus("SENT"),
                "totalFailed",    repo.countByStatus("FAILED"),
                "totalDuplicate", repo.countByStatus("DUPLICATE"),
                "sentToday",      repo.countByStatusAndCreatedAtGreaterThanEqual("SENT", todayStart)
        );
    }

    @GetMapping("/logs")
    public Page<DeliveryLog> getLogs(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return repo.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }
}
