package com.example.grannfix.offer.api.dto;

import com.example.grannfix.offer.domain.OfferStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OfferResponse(
        UUID id,
        UUID taskId,
        UUID helperId,
        BigDecimal proposedPrice,
        String message,
        OfferStatus status,
        Instant createdAt,
        Instant updatedAt,
        Instant completedAt
) {}