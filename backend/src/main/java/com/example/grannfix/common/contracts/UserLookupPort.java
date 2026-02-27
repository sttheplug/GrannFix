package com.example.grannfix.common.contracts;

import java.util.UUID;
public interface UserLookupPort {
    boolean existsActive(UUID userId);
    boolean isVerified(UUID userId);
    String displayName(UUID userId);
}