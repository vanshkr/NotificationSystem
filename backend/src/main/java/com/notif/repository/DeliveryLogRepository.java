package com.notif.repository;

import com.notif.model.DeliveryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

// JpaRepository<Entity, PrimaryKeyType> gives you save(), findAll(),
// findById(), deleteById() etc. for free — no SQL needed for basic ops.
@Repository
public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, Long> {

    // Spring Data derives the SQL from the method name:
    // SELECT * FROM delivery_log WHERE event_id = ?
    Optional<DeliveryLog> findByEventId(String eventId);

    // SELECT COUNT(*) FROM delivery_log WHERE status = ?
    // Used by StatsController for sent/failed/duplicate counts
    long countByStatus(String status);

    // Custom JPQL query — counts records created after a given timestamp
    // Used for "today's notifications" stat
    @Query("SELECT COUNT(d) FROM DeliveryLog d WHERE d.createdAt >= :since")
    long countSince(LocalDateTime since);

    long countByStatusAndCreatedAtGreaterThanEqual(String status, LocalDateTime since);

    Page<DeliveryLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
