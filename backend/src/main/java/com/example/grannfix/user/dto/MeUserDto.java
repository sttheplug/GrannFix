package com.example.grannfix.user.dto;

import java.util.UUID;

public record MeUserDto(
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
