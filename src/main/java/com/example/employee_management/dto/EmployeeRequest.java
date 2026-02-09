package com.example.employee_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotNull Long departmentId,
        BigDecimal salary,
        LocalDate joiningDate
) {
}