package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repo.DepartmentRepository;
import com.example.employee_management.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public EmployeeResponse create(EmployeeRequest request) {

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Employee employee = Employee.builder()
                .fullName(request.fullName())
                .email(request.email())
                .department(department)
                .salary(request.salary())
                .joiningDate(request.joiningDate())
                .build();

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        employee.setFullName(request.fullName());
        employee.setEmail(request.email());
        employee.setDepartment(department);
        employee.setSalary(request.salary());
        employee.setJoiningDate(request.joiningDate());

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse getById(Long id) {
        return employeeRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employeeRepository.delete(employee);
    }

    @Override
    public Page<EmployeeResponse> getAll(
            int page,
            String sortBy,
            String direction,
            Long departmentId
    ) {

        Sort sort = Sort.by(
                Sort.Direction.fromString(direction),
                sortBy
        );

        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<Employee> employees = departmentId == null
                ? employeeRepository.findAll(pageable)
                : employeeRepository.findByDepartment_Id(departmentId, pageable);

        return employees.map(this::mapToResponse);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getDepartment().getName(),
                employee.getSalary(),
                employee.getJoiningDate(),
                employee.getCreatedAt()
        );
    }
}
