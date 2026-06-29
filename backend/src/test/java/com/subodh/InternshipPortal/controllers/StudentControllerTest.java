package com.subodh.InternshipPortal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subodh.InternshipPortal.config.TestSecurityConfig;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.enums.WorkMode;
import com.subodh.InternshipPortal.modals.Application;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.*;
import com.subodh.InternshipPortal.wrapper.*;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link WebMvcTest} slice tests for {@link StudentController}.
 *
 * <p>Simulates an authenticated STUDENT principal via {@link WithMockUser}.
 * All service dependencies are replaced with Mockito mocks.
 */
@WebMvcTest(StudentController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private InternshipService internshipService;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private DepartmentService departmentService;

    @MockitoBean
    private InternshipStudentsService internshipStudentsService;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private CertificateService certificateService;

    private Users student;
    private InternshipWrapper internshipWrapper;
    private Internship internship;
    private DepartmentDetails department;

    /**
     * Sets up shared test fixtures.
     */
    @BeforeEach
    void setUp() {
        department = new DepartmentDetails();
        department.setDepartmentName("IT");

        Users instructor = new Users();
        instructor.setUserId(2L);
        instructor.setUserEmail("instructor@test.com");
        instructor.setDepartment(department);
        instructor.addRole("ROLE_INSTRUCTOR");

        internship = new Internship();
        internship.setInternshipId(1L);
        internship.setInternshipName("Java Internship");
        internship.setStartDate(LocalDate.now());
        internship.setEndDate(LocalDate.now().plusMonths(3));
        internship.setStatus(InternshipStatus.ACTIVE);
        internship.setWorkMode(WorkMode.REMOTE);
        internship.setCreatedBy(instructor);
        internship.setDepartment(department);
        internship.setRequiredSkills(Set.of("Java"));

        internshipWrapper = new InternshipWrapper(internship);

        student = new Users();
        student.setUserId(1L);
        student.setUserEmail("student@test.com");
        student.setUserName("Test Student");
        student.addRole("ROLE_STUDENT");
    }

    /**
     * Tests that {@code GET /view-internships} returns 200 with internship list.
     */
    @Test
    @DisplayName("GET /view-internships returns 200 with internship list")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void viewInternships_returns200() throws Exception {
        when(internshipService.findAll()).thenReturn(List.of(internshipWrapper));

        mockMvc.perform(get("/api/v1/students/view-internships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").isArray());
    }

    /**
     * Tests that {@code POST /apply} returns 201 when the application is submitted successfully.
     */
    @Test
    @DisplayName("POST /apply returns 201 when application submitted")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void apply_returns201_whenSuccessful() throws Exception {
        Application application = new Application();
        application.setInternship(internship);
        application.setStudent(student);

        ApplicationWrapper wrapper = new ApplicationWrapper(application);

        when(applicationService.save(any(Application.class), eq("student@test.com")))
                .thenReturn(wrapper);

        mockMvc.perform(post("/api/v1/students/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(application)))
                .andExpect(status().isCreated());
    }

    /**
     * Tests that {@code GET /has-applied/{internshipId}} returns {@code true} when an application exists.
     */
    @Test
    @DisplayName("GET /has-applied returns true when application exists")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void hasApplied_returnsTrue_whenApplicationExists() throws Exception {
        Application application = new Application();
        application.setInternship(internship);
        application.setStudent(student);
        ApplicationWrapper wrapper = new ApplicationWrapper(application);

        when(userService.findByUserEmail("student@test.com")).thenReturn(student);
        when(applicationService.existsByInternshipAndStudent(1L, 1L))
                .thenReturn(Optional.of(wrapper));

        mockMvc.perform(get("/api/v1/students/has-applied/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").value(true));
    }

    /**
     * Tests that {@code GET /has-applied/{internshipId}} returns {@code false} when no application exists.
     */
    @Test
    @DisplayName("GET /has-applied returns false when no application found")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void hasApplied_returnsFalse_whenNoApplication() throws Exception {
        when(userService.findByUserEmail("student@test.com")).thenReturn(student);
        when(applicationService.existsByInternshipAndStudent(1L, 1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/students/has-applied/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").value(false));
    }

    /**
     * Tests that {@code GET /view-submitted-applications} returns 200 with the student's applications.
     */
    @Test
    @DisplayName("GET /view-submitted-applications returns 200 with application list")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void viewSubmittedApplications_returns200() throws Exception {
        when(applicationService.findAllApplicationsByUserEmail("student@test.com"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/students/view-submitted-applications"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /get-profile-details} returns 200 with the student's profile.
     */
    @Test
    @DisplayName("GET /get-profile-details returns 200 with student profile")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void getProfileDetails_returns200() throws Exception {
        when(userService.findByUserEmail("student@test.com")).thenReturn(student);

        mockMvc.perform(get("/api/v1/students/get-profile-details"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /departments} returns 200 with department list.
     */
    @Test
    @DisplayName("GET /departments returns 200 with department list")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void departments_returns200() throws Exception {
        when(departmentService.findAllDepartments()).thenReturn(List.of("IT", "Finance"));

        mockMvc.perform(get("/api/v1/students/departments"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /selected-internships} returns 200 with the enrolled internship list.
     */
    @Test
    @DisplayName("GET /selected-internships returns 200 with enrolled internships")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void selectedInternships_returns200() throws Exception {
        when(userService.findByUserEmail("student@test.com")).thenReturn(student);
        when(internshipStudentsService.findAllByStudentId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/students/selected-internships"))
                .andExpect(status().isOk());
    }
}
