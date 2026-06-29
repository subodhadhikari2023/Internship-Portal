package com.subodh.InternshipPortal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subodh.InternshipPortal.config.TestSecurityConfig;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.enums.WorkMode;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.StudentApplication;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link WebMvcTest} slice tests for {@link InstructorController}.
 *
 * <p>Simulates an authenticated INSTRUCTOR principal via {@link WithMockUser}.
 * All service dependencies are replaced with Mockito mocks.
 */
@WebMvcTest(InstructorController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class InstructorControllerTest {

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
    private InternshipStudentsService internshipStudentsService;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private CertificateService certificateService;

    @MockitoBean
    private UserService userService;

    private InternshipWrapper internshipWrapper;
    private Users instructor;
    private DepartmentDetails department;

    /**
     * Sets up shared test fixtures used across multiple tests.
     */
    @BeforeEach
    void setUp() {
        department = new DepartmentDetails();
        department.setDepartmentName("IT");

        instructor = new Users();
        instructor.setUserId(1L);
        instructor.setUserEmail("instructor@test.com");
        instructor.setDepartment(department);
        instructor.addRole("ROLE_INSTRUCTOR");

        Internship internship = new Internship();
        internship.setInternshipId(1L);
        internship.setInternshipName("Test Internship");
        internship.setStartDate(LocalDate.now());
        internship.setEndDate(LocalDate.now().plusMonths(3));
        internship.setStatus(InternshipStatus.ACTIVE);
        internship.setWorkMode(WorkMode.REMOTE);
        internship.setCreatedBy(instructor);
        internship.setDepartment(department);
        internship.setRequiredSkills(Set.of("Java"));

        internshipWrapper = new InternshipWrapper(internship);
    }

    /**
     * Tests that {@code POST /internship} creates an internship and returns 201.
     */
    @Test
    @DisplayName("POST /internship creates internship and returns 201")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void createInternship_returns201() throws Exception {
        Internship internship = new Internship();
        internship.setInternshipName("New Internship");
        internship.setRequiredSkills(Set.of("Java"));

        when(internshipService.saveInternship(any(Internship.class), anyString()))
                .thenReturn(internshipWrapper);

        mockMvc.perform(post("/api/v1/instructors/internship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(internship)))
                .andExpect(status().isCreated());
    }

    /**
     * Tests that {@code GET /internship} returns 200 with internship list.
     */
    @Test
    @DisplayName("GET /internship returns 200 with non-empty list")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getAllInternship_returns200_withInternships() throws Exception {
        when(internshipService.findAllByInstructor()).thenReturn(List.of(internshipWrapper));

        mockMvc.perform(get("/api/v1/instructors/internship"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /internship} returns 204 when no internships exist.
     */
    @Test
    @DisplayName("GET /internship returns 204 when list is empty")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getAllInternship_returns204_whenEmpty() throws Exception {
        when(internshipService.findAllByInstructor()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/instructors/internship"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests that {@code GET /applications} returns 200 with application list.
     */
    @Test
    @DisplayName("GET /applications returns 200 with application list")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getAllApplications_returns200() throws Exception {
        when(applicationService.findAllofUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/instructors/applications"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code POST /review-applications} returns 200 with the updated wrapper.
     */
    @Test
    @DisplayName("POST /review-applications returns 200 with updated application")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void reviewApplications_returns200() throws Exception {
        Internship internship = new Internship();
        internship.setInternshipName("Test");
        internship.setCreatedBy(instructor);
        internship.setDepartment(department);

        com.subodh.InternshipPortal.modals.Application app = new com.subodh.InternshipPortal.modals.Application();
        app.setInternship(internship);

        Users student = new Users();
        student.setUserEmail("student@test.com");
        student.setUserName("Student");
        app.setStudent(student);

        StudentApplication sa = new StudentApplication(app);
        ApplicationWrapper wrapper = new ApplicationWrapper(sa);

        when(applicationService.reviewApplications(eq(StudentApplicationStatus.ACCEPTED), eq(1L)))
                .thenReturn(wrapper);

        mockMvc.perform(post("/api/v1/instructors/review-applications")
                        .param("applicationId", "1")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("ACCEPTED"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /get-profile-details} returns 200 with instructor profile.
     */
    @Test
    @DisplayName("GET /get-profile-details returns 200 with instructor profile")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getProfileDetails_returns200() throws Exception {
        InstructorWrapper wrapper = new InstructorWrapper();
        wrapper.setUserEmail("instructor@test.com");
        wrapper.setUserName("Instructor One");
        wrapper.setDepartment("IT");

        when(userService.getProfileDetails(any())).thenReturn(wrapper);

        mockMvc.perform(get("/api/v1/instructors/get-profile-details"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /internship-students} returns 200 with the student list.
     */
    @Test
    @DisplayName("GET /internship-students returns 200 with student list")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getAllInternshipStudents_returns200() throws Exception {
        when(internshipStudentsService.findAllStudentsOfInternshipsCreated(anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/instructors/internship-students"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code GET /pending-applications} returns 200 with count.
     */
    @Test
    @DisplayName("GET /pending-applications returns 200 with application count")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getAllPendingApplications_returns200() throws Exception {
        when(applicationService.findAllofUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/instructors/pending-applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").value(0));
    }

    /**
     * Tests that {@code GET /active-internships} returns 200 with the active count.
     */
    @Test
    @DisplayName("GET /active-internships returns 200 with active internship count")
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void getAllActiveInternships_returns200() throws Exception {
        when(internshipService.findAllByInstructor_ACTIVE()).thenReturn(List.of(internshipWrapper));

        mockMvc.perform(get("/api/v1/instructors/active-internships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").value(1));
    }
}
