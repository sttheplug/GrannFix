package com.example.grannfix.task.repository;

import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.model.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByCreatedBy_IdAndActiveTrue(UUID userId);
    @Query("""
        SELECT t FROM Task t
        WHERE t.active = true
          AND (:status IS NULL OR t.status = :status)
          AND (:city IS NULL OR LOWER(t.city) = LOWER(:city))
          AND (:area IS NULL OR LOWER(t.area) = LOWER(:area))
          AND (
                :cursorCreatedAt IS NULL
                OR t.createdAt < :cursorCreatedAt
                OR (t.createdAt = :cursorCreatedAt AND t.id < :cursorId)
          )
        ORDER BY t.createdAt DESC, t.id DESC
    """)
    List<Task> findActiveWithCursor(
            @Param("status") TaskStatus status,
            @Param("city") String city,
            @Param("area") String area,
            @Param("cursorCreatedAt") Instant cursorCreatedAt,
            @Param("cursorId") UUID cursorId,
            Pageable pageable
    );
}