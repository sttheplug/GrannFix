package com.example.grannfix.offer.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateOfferRequest(
        @DecimalMin(value = "0.0", inclusive = false, message = "proposedPrice must be > 0")
        BigDecimal proposedPrice,
        @Size(max = 500)
        String message
) {}