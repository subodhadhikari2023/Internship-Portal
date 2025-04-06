package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.OneTimePassword;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;

/**
 * The interface Otp service.
 */
public interface OTPService {
    /**
     * Generate otp one time password.
     *
     * @param user the user
     * @return OneTimePassword one time password
     */
    OneTimePassword generateOTP(RegistrationEntity user);

    /**
     * Verify otp boolean.
     *
     * @param userEmail the user email
     * @param otp       the otp
     * @return the boolean
     */
    boolean verifyOTP(String userEmail,String otp);

    /**
     * Generate otp for password reset one time password.
     *
     * @param email the email
     * @return the one time password
     */
    OneTimePassword generateOTPForPasswordReset(String email);
}
