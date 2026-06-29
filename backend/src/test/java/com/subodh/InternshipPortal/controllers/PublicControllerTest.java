package com.subodh.InternshipPortal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subodh.InternshipPortal.config.TestSecurityConfig;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.modals.OneTimePassword;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.OTPService;
import com.subodh.InternshipPortal.services.RegistrationService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.LoginResponse;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.wrapper.UserWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link WebMvcTest} slice tests for {@link PublicController}.
 *
 * <p>All service beans are mocked. No JWT filter runs in this context.
 */
@WebMvcTest(PublicController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private OTPService otpService;

    @MockitoBean
    private MailService mailService;

    @MockitoBean
    private RegistrationService registrationService;

    /**
     * Tests that {@code GET /api/v1/public/health-check} returns HTTP 200 OK.
     */
    @Test
    @DisplayName("GET /health-check returns 200 OK")
    void healthCheck_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/public/health-check"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that a new user registration generates an OTP and returns 200.
     */
    @Test
    @DisplayName("POST /register returns 200 when email is new")
    void register_returns200_whenEmailIsNew() throws Exception {
        RegistrationEntity reg = new RegistrationEntity();
        reg.setUserEmail("newuser@test.com");
        reg.setUserName("New User");
        reg.setUserPassword("password123");

        OneTimePassword otp = new OneTimePassword();
        otp.setOneTimePassword("123456");
        otp.setUserEmail("newuser@test.com");
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        when(userService.emailExists("newuser@test.com")).thenReturn(false);
        when(registrationService.findByEmail("newuser@test.com")).thenReturn(null);
        when(otpService.generateOTP(any(RegistrationEntity.class))).thenReturn(otp);

        mockMvc.perform(post("/api/v1/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists());
    }

    /**
     * Tests that registration returns 409 CONFLICT when the email already exists.
     */
    @Test
    @DisplayName("POST /register returns 409 when email already exists")
    void register_returns409_whenEmailAlreadyExists() throws Exception {
        RegistrationEntity reg = new RegistrationEntity();
        reg.setUserEmail("existing@test.com");
        reg.setUserName("Existing User");
        reg.setUserPassword("password");

        when(userService.emailExists("existing@test.com")).thenReturn(true);

        mockMvc.perform(post("/api/v1/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isConflict());
    }

    /**
     * Tests that OTP verification returns 200 and a success message when valid.
     */
    @Test
    @DisplayName("POST /register/verify returns 200 when OTP is valid")
    void verifyOtp_returns200_whenOtpIsValid() throws Exception {
        RegistrationEntity reg = new RegistrationEntity();
        reg.setUserEmail("student@test.com");

        when(otpService.verifyOTP("student@test.com", "123456")).thenReturn(true);
        when(registrationService.findByEmail("student@test.com")).thenReturn(reg);

        mockMvc.perform(post("/api/v1/public/register/verify")
                        .param("email", "student@test.com")
                        .param("otp", "123456"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that OTP verification returns 401 when the OTP is invalid or expired.
     */
    @Test
    @DisplayName("POST /register/verify returns 401 when OTP is invalid")
    void verifyOtp_returns401_whenOtpIsInvalid() throws Exception {
        when(otpService.verifyOTP("student@test.com", "000000")).thenReturn(false);

        mockMvc.perform(post("/api/v1/public/register/verify")
                        .param("email", "student@test.com")
                        .param("otp", "000000"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests that {@code POST /login} returns a JWT token on valid credentials.
     */
    @Test
    @DisplayName("POST /login returns JWT token on valid credentials")
    void login_returnsToken_onValidCredentials() throws Exception {
        Users loginUser = new Users();
        loginUser.setUserEmail("student@test.com");
        loginUser.setUserPassword("password");

        when(userService.verifyUserCredentials(any(Users.class))).thenReturn("mock.jwt.token");

        mockMvc.perform(post("/api/v1/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"));
    }

    /**
     * Tests that {@code GET /forget-password} returns 200 with a user wrapper.
     */
    @Test
    @DisplayName("GET /forget-password returns 200 with user info")
    void forgetPassword_returns200_withUserWrapper() throws Exception {
        Users user = new Users();
        user.setUserEmail("student@test.com");
        when(userService.forgetPassword("student@test.com")).thenReturn(new UserWrapper(user));

        mockMvc.perform(get("/api/v1/public/forget-password")
                        .param("email", "student@test.com"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that {@code POST /reset-password} returns 202 ACCEPTED on success.
     */
    @Test
    @DisplayName("POST /reset-password returns 202 when user is found")
    void resetPassword_returns202_whenUserFound() throws Exception {
        Users user = new Users();
        user.setUserEmail("student@test.com");
        when(userService.resetPasswordUserFoundByEmail("student@test.com", "newPass"))
                .thenReturn(new UserWrapper(user));

        mockMvc.perform(post("/api/v1/public/reset-password")
                        .param("email", "student@test.com")
                        .param("password", "newPass"))
                .andExpect(status().isAccepted());
    }
}
