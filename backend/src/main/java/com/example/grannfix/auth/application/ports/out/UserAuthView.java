package com.example.grannfix.auth.application.ports.out;

import java.util.UUID;

public record UserAuthView(
        UUID id,
        String phoneNumber,
        String email,
        String passwordHash,
        String name,
        String bio,
        String city,
        String area,
        String street,
        boolean active,
        boolean verified
) {}