package com.example.employee_management.service;

import com.example.employee_management.dto.LeaveNotificationMessage;
import com.example.employee_management.dto.LeaveRequestCreateRequest;
import com.example.employee_management.dto.LeaveResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.entity.LeaveRequest;
import com.example.employee_management.entity.LeaveStatus;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repo.EmployeeRepository;
import com.example.employee_management.repo.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationPublisher notificationPublisher;


    @Override
    public LeaveResponse applyLeave(LeaveRequestCreateRequest request) {

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reason(request.reason())
                .status(LeaveStatus.PENDING)
                .build();

        LeaveResponse leaveResponse = mapToResponse(leaveRequestRepository.save(leaveRequest));
        notificationPublisher.sendLeaveStatusUpdate(
                new LeaveNotificationMessage(
                        leaveRequest.getId(),
                        leaveRequest.getEmployee().getFullName(),
                        leaveRequest.getStartDate(),
                        leaveRequest.getEndDate(),
                        leaveResponse.status()
                )
        );
        return leaveResponse;
    }

    @Override
    public LeaveResponse updateStatus(Long leaveId, LeaveStatus status) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        leaveRequest.setStatus(status);

        return mapToResponse(leaveRequestRepository.save(leaveRequest));
    }

    @Override
    public List<LeaveResponse> getEmployeeLeaves(Long employeeId) {

        return leaveRequestRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LeaveResponse mapToResponse(LeaveRequest leave) {
        return new LeaveResponse(
                leave.getId(),
                leave.getEmployee().getFullName(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getStatus(),
                leave.getReason(),
                leave.getCreatedAt()
        );
    }
}

