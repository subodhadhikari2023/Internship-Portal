package com.subodh.InternshipPortal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subodh.InternshipPortal.config.TestSecurityConfig;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.DepartmentService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link WebMvcTest} slice tests for {@link AdminController}.
 *
 * <p>Uses {@link WithMockUser} to simulate an authenticated ADMIN principal
 * and mocks all service dependencies.
 */
@WebMvcTest(AdminController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private DepartmentService departmentService;

    @MockitoBean
    private UserService userService;

    /**
     * Tests that {@code GET /department} returns 200 and the list of departments.
     */
    @Test
    @DisplayName("GET /department returns 200 with department list")
    @WithMockUser(roles = "ADMIN")
    void getAllDepartment_returns200_withDepartments() throws Exception {
        when(departmentService.findAllDepartments()).thenReturn(List.of("IT", "Finance"));

        mockMvc.perform(get("/api/v1/administrator/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").isArray())
                .andExpect(jsonPath("$.entity[0]").value("IT"));
    }

    /**
     * Tests that {@code GET /department} returns 204 NO CONTENT when no departments exist.
     */
    @Test
    @DisplayName("GET /department returns 204 when no departments exist")
    @WithMockUser(roles = "ADMIN")
    void getAllDepartment_returns204_whenEmpty() throws Exception {
        when(departmentService.findAllDepartments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/administrator/department"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests that {@code POST /department} creates a department and returns 200.
     */
    @Test
    @DisplayName("POST /department creates department and returns 200")
    @WithMockUser(roles = "ADMIN")
    void addDepartment_returns200_withCreatedDepartment() throws Exception {
        DepartmentDetails dept = new DepartmentDetails();
        dept.setDepartmentName("Engineering");

        APIRequest<String> request = new APIRequest<>("Engineering");

        when(departmentService.createDepartment("Engineering")).thenReturn(dept);

        mockMvc.perform(post("/api/v1/administrator/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /users} returns 200 with the list of all users.
     */
    @Test
    @DisplayName("GET /users returns 200 with all users")
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_returns200() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/administrator/users"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /instructors} returns 200 with the list of instructors.
     */
    @Test
    @DisplayName("GET /instructors returns 200 with instructor list")
    @WithMockUser(roles = "ADMIN")
    void getAllInstructors_returns200() throws Exception {
        when(userService.findAllInstructors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/administrator/instructors"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code POST /instructors} creates an instructor and returns 201.
     */
    @Test
    @DisplayName("POST /instructors returns 201 with created instructor")
    @WithMockUser(roles = "ADMIN")
    void addInstructor_returns201() throws Exception {
        InstructorWrapper wrapper = new InstructorWrapper();
        wrapper.setUserName("New Instructor");
        wrapper.setUserEmail("newinstructor@test.com");
        wrapper.setPhoneNumber(9876543210L);
        wrapper.setDepartment("IT");

        when(userService.addInstructor(any(InstructorWrapper.class))).thenReturn(wrapper);

        mockMvc.perform(post("/api/v1/administrator/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrapper)))
                .andExpect(status().isCreated());
    }

    /**
     * Tests that {@code GET /students} returns 200 with the list of students.
     */
    @Test
    @DisplayName("GET /students returns 200 with student list")
    @WithMockUser(roles = "ADMIN")
    void getAllStudents_returns200() throws Exception {
        when(userService.findAllStudents()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/administrator/students"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /fetch-profile-details} returns 200 with an admin wrapper.
     */
    @Test
    @DisplayName("GET /fetch-profile-details returns 200 with admin profile")
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void fetchProfileDetails_returns200_withAdminProfile() throws Exception {
        Users admin = new Users();
        admin.setUserEmail("admin@test.com");
        admin.setUserName("Admin User");
        admin.addRole("ROLE_ADMIN");

        when(userService.findByUserEmail("admin@test.com")).thenReturn(admin);

        mockMvc.perform(get("/api/v1/administrator/fetch-profile-details"))
                .andExpect(status().isOk());
    }
}
