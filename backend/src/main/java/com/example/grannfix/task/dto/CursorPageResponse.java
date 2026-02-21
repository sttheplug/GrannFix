package com.example.grannfix.task.dto;

import java.util.List;

public record CursorPageResponse<T>(
        List<T> items,
        String nextCursor,
        boolean hasMore
) {}