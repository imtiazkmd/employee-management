package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repo.DepartmentRepository;
import com.example.employee_management.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department department;
    private Employee employee;
    private EmployeeRequest request;

    @BeforeEach
    void setup() {

        department = Department.builder()
                .id(1L)
                .name("IT")
                .build();

        employee = Employee.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .department(department)
                .salary(BigDecimal.valueOf(50000))
                .joiningDate(LocalDate.now())
                .build();

        request = new EmployeeRequest(
                "John Doe",
                "john@test.com",
                1L,
                BigDecimal.valueOf(50000),
                LocalDate.now()
        );
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeResponse response = employeeService.create(request);

        assertNotNull(response);
        assertEquals("John Doe", response.fullName());

        verify(notificationPublisher, times(1))
                .sendEmployeeCreated(any());
    }

    @Test
    void shouldThrowWhenDepartmentNotFoundOnCreate() {

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.create(request));

        verify(employeeRepository, never()).save(any());
    }


    @Test
    void shouldUpdateEmployeeSuccessfully() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeResponse response = employeeService.update(1L, request);

        assertNotNull(response);
        assertEquals("John Doe", response.fullName());
    }

    @Test
    void shouldThrowWhenEmployeeNotFoundOnUpdate() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.update(1L, request));
    }

    @Test
    void shouldReturnEmployeeById() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getById(1L);

        assertEquals("John Doe", response.fullName());
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getById(1L));
    }

    @Test
    void shouldDeleteEmployee() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingEmployee() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.delete(1L));
    }

    @Test
    void shouldReturnPagedEmployeesWithoutFilter() {

        Page<Employee> page = new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<EmployeeResponse> result =
                employeeService.getAll(0, "fullName", "ASC", null);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldReturnPagedEmployeesWithDepartmentFilter() {

        Page<Employee> page = new PageImpl<>(List.of(employee));

        when(employeeRepository.findByDepartment_Id(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        Page<EmployeeResponse> result =
                employeeService.getAll(0, "fullName", "ASC", 1L);

        assertEquals(1, result.getTotalElements());
    }
}

