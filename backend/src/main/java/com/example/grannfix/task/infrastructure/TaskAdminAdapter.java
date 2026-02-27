package com.example.grannfix.task.infrastructure;

import com.example.grannfix.task.domain.Task;
import com.example.grannfix.task.domain.TaskStatus;
import com.example.grannfix.task.persistence.TaskRepository;
import com.example.grannfix.user.application.port.out.TaskManagementPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskAdminAdapter implements TaskManagementPort {

    private final TaskRepository taskRepository;
    @Override
    @Transactional
    public void cancelOpenOrAssignedTasksCreatedBy(UUID userId) {
        List<Task> tasks = taskRepository.findByCreatedByIdAndStatusIn(
                userId, List.of(TaskStatus.OPEN, TaskStatus.ASSIGNED)
        );

        for (Task task : tasks) {
            task.setStatus(TaskStatus.CANCELLED);
        }
    }
}