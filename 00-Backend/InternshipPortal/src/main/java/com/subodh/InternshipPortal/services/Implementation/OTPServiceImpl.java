package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.entities.OneTimePassword;
import com.subodh.InternshipPortal.entities.RegistrationEntity;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.repositories.OTPRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService {
    private final OTPRepository otpRepository;
    private final UsersRepository usersRepository;
    private final Random random = new Random();

    @Autowired
    public OTPServiceImpl(OTPRepository otpRepository, UsersRepository usersRepository) {
        this.otpRepository = otpRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public OneTimePassword generateOTP(RegistrationEntity user) {
        otpRepository.deleteAllByUserEmail(user.getUserEmail());
        String otp = String.format("%06d", random.nextInt(1000000)); // Ensures 6-digit OTP

        OneTimePassword oneTimePassword = new OneTimePassword();
        oneTimePassword.setOneTimePassword(otp);
        oneTimePassword.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        oneTimePassword.setUserEmail(user.getUserEmail());

        otpRepository.save(oneTimePassword);
        return oneTimePassword;
    }

    @Override
    public boolean verifyOTP(String userEmail, String oneTimePassword) {
        Optional<OneTimePassword> optionalOTP = otpRepository.findByUserEmail(userEmail);
        if (optionalOTP.isEmpty()) {
            return false;
        }

        OneTimePassword storedOTP = optionalOTP.get();

        // Check if OTP is expired
        if (storedOTP.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(storedOTP);
            return false;
        }

        // Check if OTP matches
        boolean isValid = storedOTP.getOneTimePassword().equals(oneTimePassword);

        if (isValid) {
            otpRepository.delete(storedOTP); // OTP is used, remove it
        }

        return isValid;
    }
}
