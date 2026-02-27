package com.example.grannfix.user.application.port.out;

import java.util.UUID;

public interface TaskManagementPort {
    void cancelOpenOrAssignedTasksCreatedBy(UUID userId);

}