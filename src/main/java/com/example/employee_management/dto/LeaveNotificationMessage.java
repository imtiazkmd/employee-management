package com.example.employee_management.dto;

import com.example.employee_management.entity.LeaveStatus;

import java.io.Serializable;
import java.time.LocalDate;

public record LeaveNotificationMessage(
        Long leaveId,
        String employeeName,
        LocalDate startDate,
        LocalDate endDate,
        LeaveStatus status
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
