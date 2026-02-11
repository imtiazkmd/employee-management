package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentRequest;
import com.example.employee_management.dto.DepartmentResponse;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnDepartmentsForAdmin() throws Exception {

        DepartmentResponse response =
                new DepartmentResponse(1L, "IT", "Hyd", LocalDateTime.now());

        when(departmentService.getAll())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("IT"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnDepartmentsForUser() throws Exception {

        when(departmentService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateDepartmentWhenAdmin() throws Exception {

        DepartmentRequest request =
                new DepartmentRequest("Finance", "Mumbai");

        DepartmentResponse response =
                new DepartmentResponse(1L, "Finance", "Mumbai", LocalDateTime.now());

        when(departmentService.create(request))
                .thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Finance"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturn400WhenInvalidRequest() throws Exception {

        DepartmentRequest invalidRequest =
                new DepartmentRequest("", "");

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnDepartmentEmployees() throws Exception {

        EmployeeResponse employee =
                new EmployeeResponse(
                        1L,
                        "John",
                        "john@test.com",
                        "IT",
                        null,
                        null,
                        LocalDateTime.now()
                );

        when(departmentService.getDepartmentEmployees(1L))
                .thenReturn(List.of(employee));

        mockMvc.perform(get("/api/departments/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowUserToViewDepartmentEmployees() throws Exception {

        when(departmentService.getDepartmentEmployees(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/departments/1/employees"))
                .andExpect(status().isOk());
    }
}

