package com.example.employee_management.controller;


import com.example.employee_management.dto.LeaveRequestCreateRequest;
import com.example.employee_management.dto.LeaveResponse;
import com.example.employee_management.dto.LeaveStatusUpdateRequest;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.entity.LeaveRequest;
import com.example.employee_management.repo.EmployeeRepository;
import com.example.employee_management.repo.LeaveRequestRepository;
import com.example.employee_management.service.LeaveService;
import com.example.employee_management.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeRepository  employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public LeaveResponse applyLeave(
            @Valid @RequestBody LeaveRequestCreateRequest request
    ) {
        return leaveService.applyLeave(request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveResponse updateLeaveStatus(
            @PathVariable Long id,
            @Valid @RequestBody LeaveStatusUpdateRequest request
    ) {
        return leaveService.updateStatus(id, request.status());
    }

    @GetMapping("/employee/{empId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<LeaveResponse> getEmployeeLeaves(@PathVariable Long empId) {
        return leaveService.getEmployeeLeaves(empId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<LeaveRequest> getLeavesByEmployee(Long empId) {

        if (!SecurityUtil.getCurrentUsername().equals("admin")) {
            Employee employee = employeeRepository
                    .findById(empId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (!employee.getEmail().equals(SecurityUtil.getCurrentUsername())) {
                throw new AccessDeniedException("You can view only your own leave requests");
            }
        }
        return leaveRequestRepository.findByEmployee_Id(empId);
    }

}

