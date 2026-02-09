package com.example.employee_management.dto;

import java.io.Serializable;

public record EmployeeNotificationMessage (
        Long employeeId,
        String employeeName,
        String email,
        String department
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
