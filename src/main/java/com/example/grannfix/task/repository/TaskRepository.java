package com.example.grannfix.task.repository;

import com.example.grannfix.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByCreatedBy_Id(UUID createdById);
}