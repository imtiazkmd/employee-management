package com.example.employee_management.dto;



import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LeaveRequestCreateRequest(
        @NotNull Long employeeId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        String reason
) {
}

