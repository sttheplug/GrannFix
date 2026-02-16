package com.example.grannfix.user;

import com.example.grannfix.user.model.Role;
import com.example.grannfix.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumberAndActiveTrue(String phoneNumber);
    Optional<User> findByIdAndActiveTrue(UUID id);
}
