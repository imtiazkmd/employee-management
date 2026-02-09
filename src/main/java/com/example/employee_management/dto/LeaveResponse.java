package com.example.employee_management.dto;



import com.example.employee_management.entity.LeaveStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveResponse(
        Long id,
        String employeeName,
        LocalDate startDate,
        LocalDate endDate,
        LeaveStatus status,
        String reason,
        LocalDateTime createdAt
) {
}

