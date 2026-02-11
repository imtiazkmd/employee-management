package com.example.employee_management.service;

import com.example.employee_management.dto.LeaveRequestCreateRequest;
import com.example.employee_management.dto.LeaveResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.entity.LeaveRequest;
import com.example.employee_management.entity.LeaveStatus;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repo.EmployeeRepository;
import com.example.employee_management.repo.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    private Employee employee;
    private LeaveRequest leaveRequest;

    @BeforeEach
    void setup() {
        employee = Employee.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@test.com")
                .build();

        leaveRequest = LeaveRequest.builder()
                .id(10L)
                .employee(employee)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .reason("Vacation")
                .status(LeaveStatus.PENDING)
                .build();
    }

    @Test
    void shouldApplyLeaveSuccessfully() {

        LeaveRequestCreateRequest request =
                new LeaveRequestCreateRequest(
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(2),
                        "Vacation"
                );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRequestRepository.save(any(LeaveRequest.class)))
                .thenReturn(leaveRequest);

        LeaveResponse response = leaveService.applyLeave(request);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(LeaveStatus.PENDING);
        assertThat(response.employeeName()).isEqualTo("John Doe");

        verify(notificationPublisher, times(1))
                .sendLeaveStatusUpdate(any());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        LeaveRequestCreateRequest request =
                new LeaveRequestCreateRequest(
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(2),
                        "Vacation"
                );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> leaveService.applyLeave(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found");

        verify(leaveRequestRepository, never()).save(any());
        verify(notificationPublisher, never()).sendLeaveStatusUpdate(any());
    }


    @Test
    void shouldUpdateLeaveStatusSuccessfully() {

        when(leaveRequestRepository.findById(10L))
                .thenReturn(Optional.of(leaveRequest));

        when(leaveRequestRepository.save(any(LeaveRequest.class)))
                .thenReturn(leaveRequest);

        LeaveResponse response =
                leaveService.updateStatus(10L, LeaveStatus.APPROVED);

        assertThat(response.status()).isEqualTo(LeaveStatus.APPROVED);
    }

    @Test
    void shouldThrowExceptionWhenLeaveNotFound() {

        when(leaveRequestRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                leaveService.updateStatus(99L, LeaveStatus.APPROVED)
        ).isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Leave request not found");

        verify(leaveRequestRepository, never()).save(any());
    }

    @Test
    void shouldReturnEmployeeLeaves() {

        when(leaveRequestRepository.findByEmployee_Id(1L))
                .thenReturn(List.of(leaveRequest));

        List<LeaveResponse> responses =
                leaveService.getEmployeeLeaves(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).employeeName())
                .isEqualTo("John Doe");
    }

    @Test
    void shouldReturnEmptyListWhenNoLeavesFound() {

        when(leaveRequestRepository.findByEmployee_Id(1L))
                .thenReturn(List.of());

        List<LeaveResponse> responses =
                leaveService.getEmployeeLeaves(1L);

        assertThat(responses).isEmpty();
    }
}

