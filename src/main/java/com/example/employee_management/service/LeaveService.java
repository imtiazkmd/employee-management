package com.example.employee_management.service;



import com.example.employee_management.dto.LeaveRequestCreateRequest;
import com.example.employee_management.dto.LeaveResponse;
import com.example.employee_management.entity.LeaveStatus;

import java.util.List;

public interface LeaveService {

    LeaveResponse applyLeave(LeaveRequestCreateRequest request);

    LeaveResponse updateStatus(Long leaveId, LeaveStatus status);

    List<LeaveResponse> getEmployeeLeaves(Long employeeId);
}

