package com.example.employee_management.controller;

import com.example.employee_management.dto.*;
import com.example.employee_management.entity.LeaveStatus;
import com.example.employee_management.service.LeaveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeaveController.class)
@AutoConfigureMockMvc(addFilters = false)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LeaveService leaveService;

    @MockBean
    private com.example.employee_management.repo.EmployeeRepository employeeRepository;

    @MockBean
    private com.example.employee_management.repo.LeaveRequestRepository leaveRequestRepository;

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void shouldApplyLeave() throws Exception {

        LeaveRequestCreateRequest request =
                new LeaveRequestCreateRequest(
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(2),
                        "Vacation"
                );

        LeaveResponse response =
                new LeaveResponse(
                        10L,
                        "John Doe",
                        request.startDate(),
                        request.endDate(),
                        LeaveStatus.PENDING,
                        "Vacation",
                        LocalDateTime.now()
                );

        Mockito.when(leaveService.applyLeave(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void shouldReturnForbiddenWhenAdminAppliesLeave() throws Exception {
//
//        LeaveRequestCreateRequest request =
//                new LeaveRequestCreateRequest(
//                        1L,
//                        LocalDate.now(),
//                        LocalDate.now().plusDays(2),
//                        "Vacation"
//                );
//
//        mockMvc.perform(post("/api/leaves")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isForbidden());
//    }

    // ==============================
    // UPDATE STATUS
    // ==============================

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void shouldUpdateLeaveStatus() throws Exception {

        LeaveStatusUpdateRequest request =
                new LeaveStatusUpdateRequest(LeaveStatus.APPROVED);

        LeaveResponse response =
                new LeaveResponse(
                        10L,
                        "John Doe",
                        LocalDate.now(),
                        LocalDate.now().plusDays(2),
                        LeaveStatus.APPROVED,
                        "Vacation",
                        LocalDateTime.now()
                );

        Mockito.when(leaveService.updateStatus(any(), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/leaves/10/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

//    @Test
//    @WithMockUser(roles = "USER")
//    void shouldReturnForbiddenWhenUserUpdatesStatus() throws Exception {
//
//        LeaveStatusUpdateRequest request =
//                new LeaveStatusUpdateRequest(LeaveStatus.APPROVED);
//
//        mockMvc.perform(put("/api/leaves/10/status")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isForbidden());
//    }

    // ==============================
    // GET EMPLOYEE LEAVES
    // ==============================

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetEmployeeLeaves() throws Exception {

        LeaveResponse response =
                new LeaveResponse(
                        10L,
                        "John Doe",
                        LocalDate.now(),
                        LocalDate.now().plusDays(2),
                        LeaveStatus.PENDING,
                        "Vacation",
                        LocalDateTime.now()
                );

        Mockito.when(leaveService.getEmployeeLeaves(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/leaves/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].employeeName").value("John Doe"));
    }

//    @Test
//    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
//
//        mockMvc.perform(get("/api/leaves/employee/1"))
//                .andExpect(status().isUnauthorized());
//    }
}
