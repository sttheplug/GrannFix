package com.example.grannfix.user.dto;

import java.util.UUID;

public record PublicUserDto(
        UUID id,
        String name,
        String bio,
        String area,
        Double ratingAverage,
        Integer ratingCount,
        String phoneNumber // optional, could be null
) {}
