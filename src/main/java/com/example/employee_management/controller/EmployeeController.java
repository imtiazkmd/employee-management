package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repo.EmployeeRepository;
import com.example.employee_management.service.EmployeeService;
import com.example.employee_management.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    /**
     * GET /api/employees
     * Supports pagination, sorting, filtering
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Page<EmployeeResponse> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) Long departmentId
    ) {
        return employeeService.getAll(page, sortBy, direction, departmentId);
    }

    /**
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public EmployeeResponse getEmployee(@PathVariable Long id) {
        return employeeService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Employee getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (SecurityUtil.getCurrentUsername().equals("admin")) {
            return employee;
        }

        if (!employee.getEmail().equals(SecurityUtil.getCurrentUsername())) {
            throw new AccessDeniedException("You can access only your own employee data");
        }

        return employee;
    }


    /**
     * POST /api/employees
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse createEmployee(
            @Valid @RequestBody EmployeeRequest request
    ) {
        return employeeService.create(request);
    }

    /**
     * PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ) {
        return employeeService.update(id, request);
    }

    /**
     * DELETE /api/employees/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
    }
}

