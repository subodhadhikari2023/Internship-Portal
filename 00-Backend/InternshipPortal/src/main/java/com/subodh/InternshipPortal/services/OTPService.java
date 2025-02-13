package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.OneTimePassword;
import com.subodh.InternshipPortal.entities.Users;

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
    OneTimePassword generateOTP(Users user);

    /**
     * Verify otp boolean.
     *
     * @param userEmail the user email
     * @param otp       the otp
     * @return the boolean
     */
    boolean verifyOTP(String userEmail,String otp);
}
