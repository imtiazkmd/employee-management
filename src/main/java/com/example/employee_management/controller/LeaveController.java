package com.example.employee_management.controller;


import com.example.employee_management.dto.LeaveRequestCreateRequest;
import com.example.employee_management.dto.LeaveResponse;
import com.example.employee_management.dto.LeaveStatusUpdateRequest;
import com.example.employee_management.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

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
}

