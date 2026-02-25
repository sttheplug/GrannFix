package com.example.grannfix.task.service;

import com.example.grannfix.task.dto.CursorPageResponse;
import com.example.grannfix.task.dto.TaskCursor;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.mapper.TaskMapper;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.model.TaskStatus;
import com.example.grannfix.task.repository.TaskRepository;
import com.example.grannfix.task.util.CursorCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskQueryService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public CursorPageResponse<TaskResponse> listTasks(
            String cursor,
            int limit,
            TaskStatus status,
            String city,
            String area
    ) {
        int safeLimit = Math.min(Math.max(limit, 1), 50);

        TaskCursor decoded = null;
        if (cursor != null && !cursor.isBlank()) {
            decoded = CursorCodec.decode(cursor);
        }

        Pageable pageable = PageRequest.of(0, safeLimit + 1);
        List<Task> rows = taskRepository.findActiveWithCursor(
                status,
                blankToNull(city),
                blankToNull(area),
                decoded != null ? decoded.createdAt() : null,
                decoded != null ? decoded.id() : null,
                pageable
        );

        boolean hasMore = rows.size() > safeLimit;
        List<Task> page = hasMore ? rows.subList(0, safeLimit) : rows;

        List<TaskResponse> items = page.stream()
                .map(TaskMapper::toResponse)
                .toList();

        String nextCursor = null;
        if (hasMore && !page.isEmpty()) {
            Task last = page.get(page.size() - 1);
            nextCursor = CursorCodec.encode(new TaskCursor(last.getCreatedAt(), last.getId()));
        }

        return new CursorPageResponse<>(items, nextCursor, hasMore);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}