package com.subodh.InternshipPortal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subodh.InternshipPortal.config.TestSecurityConfig;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.UserWrapper;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link WebMvcTest} slice tests for {@link CommonController}.
 *
 * <p>Tests the shared password validation and reset endpoints available to all
 * authenticated roles.
 */
@WebMvcTest(CommonController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class CommonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserService userService;

    /**
     * Tests that {@code POST /validate-password} returns 200 with {@code true}
     * when the supplied password matches the stored one.
     */
    @Test
    @DisplayName("POST /validate-password returns 200 with true when password matches")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void validatePassword_returns200True_whenMatches() throws Exception {
        when(userService.validatePassword("student@test.com", "correctPassword")).thenReturn(true);

        APIRequest<String> request = new APIRequest<>("correctPassword");

        mockMvc.perform(post("/api/v1/common/validate-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").value(true));
    }

    /**
     * Tests that {@code POST /validate-password} returns 200 with {@code false}
     * when the supplied password does not match.
     */
    @Test
    @DisplayName("POST /validate-password returns 200 with false when password does not match")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void validatePassword_returns200False_whenMismatch() throws Exception {
        when(userService.validatePassword("student@test.com", "wrongPassword")).thenReturn(false);

        APIRequest<String> request = new APIRequest<>("wrongPassword");

        mockMvc.perform(post("/api/v1/common/validate-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entity").value(false));
    }

    /**
     * Tests that {@code POST /reset-password} returns 202 ACCEPTED when the user is found.
     */
    @Test
    @DisplayName("POST /reset-password returns 202 when user is found and password is reset")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void resetPassword_returns202_whenUserFound() throws Exception {
        Users user = new Users();
        user.setUserEmail("student@test.com");
        user.setUserName("Test Student");

        when(userService.resetPasswordUserFoundByEmail(anyString(), anyString()))
                .thenReturn(new UserWrapper(user));

        APIRequest<String> request = new APIRequest<>("newSecurePassword");

        mockMvc.perform(post("/api/v1/common/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    /**
     * Tests that {@code POST /reset-password} returns 406 NOT ACCEPTABLE when the user is not found.
     */
    @Test
    @DisplayName("POST /reset-password returns 406 when user is not found")
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void resetPassword_returns406_whenUserNotFound() throws Exception {
        when(userService.resetPasswordUserFoundByEmail(anyString(), anyString())).thenReturn(null);

        APIRequest<String> request = new APIRequest<>("somePassword");

        mockMvc.perform(post("/api/v1/common/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable());
    }
}
