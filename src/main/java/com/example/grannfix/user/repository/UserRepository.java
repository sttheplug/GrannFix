package com.example.grannfix.user.repository;

import com.example.grannfix.user.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndActiveTrue(UUID id);
    boolean existsByIdAndActiveTrue(UUID uuid);
    boolean existsByEmail(String email);
}
