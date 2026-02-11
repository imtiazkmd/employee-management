package com.example.employee_management.dto;

import java.time.Instant;

public record ApiError(
        int status,
        String message,
        Instant timestamp
) {
}
