package com.example.employee_management.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String fullName,
        String email,
        String departmentName,
        BigDecimal salary,
        LocalDate joiningDate,
        LocalDateTime createdAt
) {
}