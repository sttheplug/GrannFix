package com.example.grannfix.auth.api.dto;

import java.util.UUID;

public record AuthUserDto(
        UUID id,
        String phoneNumber,
        String email,
        String name,
        String bio,
        String city,
        String area,
        String street,
        boolean verified
) {}