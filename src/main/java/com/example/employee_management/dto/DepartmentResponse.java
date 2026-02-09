package com.example.employee_management.dto;


import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        String name,
        String location,
        LocalDateTime createdAt
) {
}
