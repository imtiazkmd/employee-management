package com.example.employee_management.service;

import com.example.employee_management.dto.DepartmentRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.ResourceAlreadyExistsException;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repo.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    @BeforeEach
    void setup() {
        department = Department.builder()
                .id(1L)
                .name("IT")
                .location("Hyderabad")
                .build();
    }


    @Test
    void shouldCreateDepartmentSuccessfully() {

        DepartmentRequest request = new DepartmentRequest("IT", "Hyderabad");

        when(departmentRepository.existsByNameIgnoreCase("IT")).thenReturn(false);
        when(departmentRepository.save(any())).thenReturn(department);

        DepartmentResponse response = departmentService.create(request);

        assertThat(response.name()).isEqualTo("IT");
        assertThat(response.location()).isEqualTo("Hyderabad");

        verify(departmentRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDepartmentAlreadyExists() {

        DepartmentRequest request = new DepartmentRequest("IT", "Hyderabad");

        when(departmentRepository.existsByNameIgnoreCase("IT")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.create(request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Department already exists");

        verify(departmentRepository, never()).save(any());
    }


    @Test
    void shouldReturnAllDepartments() {

        when(departmentRepository.findAll()).thenReturn(List.of(department));

        List<DepartmentResponse> result = departmentService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("IT");
    }

    @Test
    void shouldReturnEmptyListWhenNoDepartments() {

        when(departmentRepository.findAll()).thenReturn(List.of());

        List<DepartmentResponse> result = departmentService.getAll();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnDepartmentEmployees() {

        Employee emp = Employee.builder()
                .id(10L)
                .fullName("John Doe")
                .email("john@test.com")
                .salary(BigDecimal.valueOf(50000))
                .joiningDate(LocalDate.now())
                .department(department)
                .build();

        department.setEmployees(List.of(emp));

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        List<EmployeeResponse> employees = departmentService.getDepartmentEmployees(1L);

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).fullName()).isEqualTo("John Doe");
    }

    @Test
    void shouldReturnEmptyEmployeeList() {

        department.setEmployees(List.of());

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        List<EmployeeResponse> employees = departmentService.getDepartmentEmployees(1L);

        assertThat(employees).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDepartmentNotFound() {

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                departmentService.getDepartmentEmployees(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }
}

