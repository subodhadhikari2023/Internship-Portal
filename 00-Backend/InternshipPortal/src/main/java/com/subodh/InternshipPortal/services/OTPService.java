package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.OneTimePassword;
import com.subodh.InternshipPortal.entities.Users;

public interface OTPService {
    /**
     * @param user
     * @return OneTimePassword
     */
    OneTimePassword generateOTP(Users user);

    boolean verifyOTP(String userEmail,String otp);
}
