package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.OneTimePassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OTPRepository} using an in-memory H2 database.
 *
 * <p>Covers OTP retrieval by email and bulk-delete by email.
 */
@DataJpaTest
@ActiveProfiles("test")
class OTPRepositoryTest {

    @Autowired
    private OTPRepository otpRepository;

    /**
     * Clears all OTP records before each test.
     */
    @BeforeEach
    void setUp() {
        otpRepository.deleteAll();
    }

    /**
     * Tests that a saved OTP is found by its user email.
     */
    @Test
    @DisplayName("findByUserEmail returns OTP when email matches")
    void findByUserEmail_returnsOtp_whenEmailExists() {
        OneTimePassword otp = new OneTimePassword();
        otp.setUserEmail("user@test.com");
        otp.setOneTimePassword("123456");
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        Optional<OneTimePassword> found = otpRepository.findByUserEmail("user@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getOneTimePassword()).isEqualTo("123456");
    }

    /**
     * Tests that querying by an unknown email returns empty.
     */
    @Test
    @DisplayName("findByUserEmail returns empty when email does not exist")
    void findByUserEmail_returnsEmpty_whenEmailDoesNotExist() {
        Optional<OneTimePassword> found = otpRepository.findByUserEmail("nobody@test.com");
        assertThat(found).isEmpty();
    }

    /**
     * Tests that {@code deleteAllByUserEmail} removes all OTPs for a given email.
     */
    @Test
    @DisplayName("deleteAllByUserEmail removes all OTPs for the given email")
    void deleteAllByUserEmail_removesOtps_forGivenEmail() {
        OneTimePassword otp1 = new OneTimePassword();
        otp1.setUserEmail("user@test.com");
        otp1.setOneTimePassword("111111");
        otp1.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp1);

        otpRepository.deleteAllByUserEmail("user@test.com");
        Optional<OneTimePassword> found = otpRepository.findByUserEmail("user@test.com");
        assertThat(found).isEmpty();
    }
}
