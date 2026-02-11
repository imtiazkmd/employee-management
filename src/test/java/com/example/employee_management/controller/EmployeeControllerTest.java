package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.repo.EmployeeRepository;
import com.example.employee_management.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldGetEmployees() throws Exception {

        when(employeeService.getAll(anyInt(), anyString(), anyString(), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userShouldGetEmployees() throws Exception {

        when(employeeService.getAll(anyInt(), anyString(), anyString(), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldCreateEmployee() throws Exception {

        EmployeeRequest request = new EmployeeRequest(
                "John Doe",
                "john@test.com",
                1L,
                BigDecimal.valueOf(50000),
                LocalDate.now()
        );

        EmployeeResponse response = new EmployeeResponse(
                1L,
                "John Doe",
                "john@test.com",
                "IT",
                BigDecimal.valueOf(50000),
                LocalDate.now(),
                null
        );

        when(employeeService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldUpdateEmployee() throws Exception {

        EmployeeRequest request = new EmployeeRequest(
                "Updated Name",
                "updated@test.com",
                1L,
                BigDecimal.valueOf(60000),
                LocalDate.now()
        );

        when(employeeService.update(eq(1L), any()))
                .thenReturn(new EmployeeResponse(
                        1L,
                        "Updated Name",
                        "updated@test.com",
                        "IT",
                        BigDecimal.valueOf(60000),
                        LocalDate.now(),
                        null
                ));

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldDeleteEmployee() throws Exception {

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

}

