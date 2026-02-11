package com.example.employee_management.service;

import com.example.employee_management.dto.DepartmentRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Department;
import com.example.employee_management.exception.ResourceAlreadyExistsException;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse create(DepartmentRequest request) {

        if (departmentRepository.existsByNameIgnoreCase(request.name())) {
            throw new ResourceAlreadyExistsException(
                    "Department already exists with name: " + request.name()
            );
        }

        Department department = Department.builder()
                .name(request.name())
                .location(request.location())
                .build();

        Department saved = departmentRepository.save(department);

        return new DepartmentResponse(
                saved.getId(),
                saved.getName(),
                saved.getLocation(),
                saved.getCreatedAt()
        );
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(d -> new DepartmentResponse(
                        d.getId(),
                        d.getName(),
                        d.getLocation(),
                        d.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public List<EmployeeResponse> getDepartmentEmployees(Long departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        return department.getEmployees()
                .stream()
                .map(emp -> new EmployeeResponse(
                        emp.getId(),
                        emp.getFullName(),
                        emp.getEmail(),
                        department.getName(),
                        emp.getSalary(),
                        emp.getJoiningDate(),
                        emp.getCreatedAt()
                ))
                .toList();
    }
}

