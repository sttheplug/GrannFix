package com.example.grannfix.task.application.port.out;

import java.util.UUID;

public interface UserDirectory {
    boolean existsActive(UUID userId);
    boolean isVerified(UUID userId);
    String getDisplayName(UUID userId);
}