package com.example.grannfix.auth.application.ports.out;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthPort {
    Optional<UserAuthView> findByEmail(String email);
    Optional<UserAuthView> findByPhone(String phone);
    Optional<UserAuthView> findById(UUID userId);
    boolean existsByEmail(String email);
    UserAuthView createUser(CreateUserCommand cmd);
    void markVerified(UUID userId);
    void updatePassword(UUID userId, String encodedPassword);
}