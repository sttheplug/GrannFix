package com.example.grannfix.task.service;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.mapper.TaskMapper;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.model.TaskStatus;
import com.example.grannfix.task.repository.TaskRepository;
import com.example.grannfix.user.model.User;
import com.example.grannfix.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper mapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    @Transactional
    public TaskResponse addTask(UUID createdById, CreateTaskRequest req) {
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + createdById));

        if (!createdBy.isVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Phone number must be verified (OTP) before creating tasks."
            );
        }
        BigDecimal price = req.offeredPrice();
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("offeredPrice cannot be negative");
        }

        Task task = Task.builder()
                .createdBy(createdBy)
                .title(req.title().trim())
                .description(req.description().trim())
                .city(req.city().trim())
                .area(req.area().trim())
                .street(req.street() != null ? req.street().trim() : null)
                .offeredPrice(price)
                .build();
        Task saved = taskRepository.save(task);
        return mapper.toResponse(saved);
    }
    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(UUID userId) {
        if (!userRepository.existsByIdAndActiveTrue(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return taskRepository.findByCreatedBy_IdAndActiveTrue(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteMyTask(UUID userId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getCreatedBy().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only delete your own tasks."
            );
        }

        if (task.getStatus() == TaskStatus.ASSIGNED ||
                task.getStatus() == TaskStatus.COMPLETED) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Assigned or completed tasks cannot be deleted."
            );
        }
        task.setActive(false);
    }

}
