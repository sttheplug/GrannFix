package com.example.grannfix.user.dto;

import java.util.UUID;

public record PublicUserDto(
        UUID id,
        String name,
        String bio,
        String city,
        String area,
        Double ratingAverage,
        Integer ratingCount,
        String phoneNumber
) {}
