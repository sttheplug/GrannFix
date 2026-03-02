package com.example.grannfix.common.contracts;

import java.util.Optional;
import java.util.UUID;

public interface TaskOfferPort {
    Optional<TaskOfferView> findById(UUID taskId);
}