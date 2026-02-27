package com.example.grannfix.task.api.dto;

import java.time.Instant;
import java.util.UUID;

public record TaskCursor(Instant createdAt, UUID id) {}