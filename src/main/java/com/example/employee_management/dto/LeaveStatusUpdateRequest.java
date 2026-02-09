package com.example.employee_management.dto;


import com.example.employee_management.entity.LeaveStatus;
import jakarta.validation.constraints.NotNull;

public record LeaveStatusUpdateRequest(
        @NotNull LeaveStatus status
) {
}

