package com.example.grannfix.task.service;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.dto.TaskDetailResponse;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.dto.UpdateTaskRequest;
import com.example.grannfix.task.mapper.TaskMapper;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.model.TaskStatus;
import com.example.grannfix.task.repository.TaskRepository;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.model.User;
import com.example.grannfix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
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
        return TaskMapper.toResponse(saved);
    }
    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(UUID userId) {
        if (!userRepository.existsByIdAndActiveTrue(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return taskRepository.findByCreatedBy_IdAndActiveTrue(userId)
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskDetailResponse getTaskById(UUID userId, UUID taskId) {
        Task task = taskRepository.findByIdAndActiveTrue(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        boolean isOwner = task.getCreatedBy().getId().equals(userId);
        if (!isOwner && task.getStatus() != TaskStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return TaskMapper.toDetailResponse(task, userId);
    }

    @Transactional
    public TaskResponse updateMyTask(UUID userId, UUID taskId, UpdateTaskRequest req) {
        Task task = getTaskOrThrow(taskId);
        if (!task.getCreatedBy().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own tasks.");
        }

        if (!task.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inactive tasks cannot be updated.");
        }

        if (task.getStatus() == TaskStatus.ASSIGNED || task.getStatus() == TaskStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigned or completed tasks cannot be updated.");
        }

        if (req.offeredPrice() != null) {
            task.setOfferedPrice(req.offeredPrice());
        }

        if (req.title() != null) {
            String v = req.title().trim();
            if (v.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title cannot be blank");
            task.setTitle(v);
        }
        if (req.description() != null) {
            String v = req.description().trim();
            if (v.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description cannot be blank");
            task.setDescription(v);
        }
        if (req.city() != null) {
            String v = req.city().trim();
            if (v.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "city cannot be blank");
            task.setCity(v);
        }
        if (req.area() != null) {
            String v = req.area().trim();
            if (v.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "area cannot be blank");
            task.setArea(v);
        }
        if (req.street() != null) {
            String v = req.street().trim();
            task.setStreet(v.isEmpty() ? null : v);
        }
        Task saved = taskRepository.save(task);
        return TaskMapper.toResponse(saved);
    }

    @Transactional
    public void cancelMyTask(UUID userId, UUID taskId){
        Task task = getTaskOrThrow(taskId);
        if (!task.getCreatedBy().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only cancel your own tasks."
            );
        }
        if (!task.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Task is already inactive."
            );
        }
        if (task.getStatus() == TaskStatus.COMPLETED ||
                task.getStatus() == TaskStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Task cannot be cancelled."
            );
        }
        task.setStatus(TaskStatus.CANCELLED);
    }

    @Transactional
    public void deleteMyTask(UUID userId, UUID taskId) {
        Task task = getTaskOrThrow(taskId);
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
    private Task getTaskOrThrow(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}
