package com.example.grannfix.task.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateTaskRequest(

        @Size(max = 120)
        String title,

        @Size(max = 1000)
        String description,

        String city,

        String area,

        String street,

        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal offeredPrice
) {}