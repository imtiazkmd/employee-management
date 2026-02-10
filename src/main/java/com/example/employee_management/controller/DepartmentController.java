package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<DepartmentResponse> getDepartments() {
        return departmentService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentResponse createDepartment(
            @Valid @RequestBody DepartmentRequest request
    ) {
        return departmentService.create(request);
    }

    @GetMapping("/{id}/employees")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<EmployeeResponse> getDepartmentEmployees(@PathVariable Long id) {
        return departmentService.getDepartmentEmployees(id);
    }
}

