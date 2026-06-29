package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.OneTimePassword;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.OTPRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.Implementation.OTPServiceImpl;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OTPServiceImpl}.
 *
 * <p>Verifies OTP generation, verification (valid, expired, wrong code),
 * and password-reset OTP flow.
 */
@ExtendWith(MockitoExtension.class)
class OTPServiceImplTest {

    @Mock
    private OTPRepository otpRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private OTPServiceImpl otpService;

    /**
     * Tests that {@code generateOTP} deletes existing OTPs and saves a new one.
     */
    @Test
    @DisplayName("generateOTP deletes old OTPs and saves a new one")
    void generateOTP_deletesOldAndSavesNew() {
        RegistrationEntity user = new RegistrationEntity();
        user.setUserEmail("student@test.com");

        ArgumentCaptor<OneTimePassword> captor = ArgumentCaptor.forClass(OneTimePassword.class);
        when(otpRepository.save(any(OneTimePassword.class))).thenAnswer(inv -> inv.getArgument(0));

        OneTimePassword result = otpService.generateOTP(user);

        verify(otpRepository).deleteAllByUserEmail("student@test.com");
        verify(otpRepository).save(captor.capture());
        assertThat(captor.getValue().getUserEmail()).isEqualTo("student@test.com");
        assertThat(result.getOneTimePassword()).hasSize(6);
    }

    /**
     * Tests that {@code verifyOTP} returns {@code true} for a matching, non-expired OTP.
     */
    @Test
    @DisplayName("verifyOTP returns true for valid, non-expired OTP")
    void verifyOTP_returnsTrue_whenOtpIsValidAndNotExpired() {
        OneTimePassword stored = new OneTimePassword();
        stored.setUserEmail("student@test.com");
        stored.setOneTimePassword("654321");
        stored.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        when(otpRepository.findByUserEmail("student@test.com")).thenReturn(Optional.of(stored));

        boolean result = otpService.verifyOTP("student@test.com", "654321");
        assertThat(result).isTrue();
        verify(otpRepository).delete(stored);
    }

    /**
     * Tests that {@code verifyOTP} returns {@code false} when no OTP record exists.
     */
    @Test
    @DisplayName("verifyOTP returns false when no OTP is found")
    void verifyOTP_returnsFalse_whenNoOtpFound() {
        when(otpRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());
        assertThat(otpService.verifyOTP("nobody@test.com", "123456")).isFalse();
    }

    /**
     * Tests that {@code verifyOTP} returns {@code false} and deletes the OTP when it is expired.
     */
    @Test
    @DisplayName("verifyOTP returns false and deletes OTP when expired")
    void verifyOTP_returnsFalse_whenOtpExpired() {
        OneTimePassword expired = new OneTimePassword();
        expired.setUserEmail("student@test.com");
        expired.setOneTimePassword("123456");
        expired.setExpiryTime(LocalDateTime.now().minusMinutes(1));

        when(otpRepository.findByUserEmail("student@test.com")).thenReturn(Optional.of(expired));

        boolean result = otpService.verifyOTP("student@test.com", "123456");
        assertThat(result).isFalse();
        verify(otpRepository).delete(expired);
    }

    /**
     * Tests that {@code verifyOTP} returns {@code false} when the OTP code does not match.
     */
    @Test
    @DisplayName("verifyOTP returns false when OTP code does not match")
    void verifyOTP_returnsFalse_whenOtpMismatch() {
        OneTimePassword stored = new OneTimePassword();
        stored.setUserEmail("student@test.com");
        stored.setOneTimePassword("111111");
        stored.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        when(otpRepository.findByUserEmail("student@test.com")).thenReturn(Optional.of(stored));

        assertThat(otpService.verifyOTP("student@test.com", "999999")).isFalse();
    }

    /**
     * Tests that {@code generateOTPForPasswordReset} sends a password-reset email.
     */
    @Test
    @DisplayName("generateOTPForPasswordReset sends password reset email")
    void generateOTPForPasswordReset_sendsResetEmail() {
        Users user = new Users();
        user.setUserEmail("student@test.com");

        when(usersRepository.findByUserEmail("student@test.com")).thenReturn(user);
        when(otpRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        otpService.generateOTPForPasswordReset("student@test.com");

        verify(mailService).sendPasswordResetMail(eq("student@test.com"), anyString());
    }
}
