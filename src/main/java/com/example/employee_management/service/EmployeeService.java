package com.example.employee_management.service;


import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import org.springframework.data.domain.Page;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse update(Long id, EmployeeRequest request);

    EmployeeResponse getById(Long id);

    void delete(Long id);

    Page<EmployeeResponse> getAll(
            int page,
            String sortBy,
            String direction,
            Long departmentId
    );
}
