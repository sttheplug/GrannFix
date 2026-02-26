package com.example.grannfix.user.persistence;

import com.example.grannfix.user.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByIdAndActiveTrue(UUID uuid);
    boolean existsByEmail(String email);
}
