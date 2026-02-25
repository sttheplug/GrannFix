package com.example.grannfix.user.dto;

import com.example.grannfix.user.model.Role;
import java.time.Instant;
import java.util.UUID;

public record AdminUserDto(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String city,
        String area,
        boolean active,
        boolean verified,
        Role role,
        Double ratingAverage,
        Integer ratingCount,
        Instant createdAt,
        Instant updatedAt
) {}
