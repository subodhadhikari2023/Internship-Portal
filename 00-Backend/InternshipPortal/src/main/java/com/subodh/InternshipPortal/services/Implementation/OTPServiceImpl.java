package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.modals.OneTimePassword;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.repositories.OTPRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * The type Otp service.
 */
@Service
public class OTPServiceImpl implements OTPService {
    private final OTPRepository otpRepository;
    private final UsersRepository usersRepository;
    private final Random random = new Random();
    private final MailService mailService;

    /**
     * Instantiates a new Otp service.
     *
     * @param otpRepository   the otp repository
     * @param usersRepository the users repository
     * @param mailService     the mail service
     */
    @Autowired
    public OTPServiceImpl(OTPRepository otpRepository, UsersRepository usersRepository, MailService mailService) {
        this.otpRepository = otpRepository;
        this.usersRepository = usersRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public OneTimePassword generateOTP(RegistrationEntity user) {
        return getOneTimePassword(user.getUserEmail());
    }

    private OneTimePassword getOneTimePassword(String userEmail) {
        otpRepository.deleteAllByUserEmail(userEmail);
        String otp = String.format("%06d", random.nextInt(1000000)); // Ensures 6-digit OTP

        OneTimePassword oneTimePassword = new OneTimePassword();
        oneTimePassword.setOneTimePassword(otp);
        oneTimePassword.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        oneTimePassword.setUserEmail(userEmail);

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

    @Override
    @Transactional
    public OneTimePassword generateOTPForPasswordReset(String email) {
        Users user = usersRepository.findByUserEmail(email);
        OneTimePassword oneTimePassword = getOneTimePassword(user.getUserEmail());
        mailService.sendPasswordResetMail(user.getUserEmail(), oneTimePassword.getOneTimePassword());
        return oneTimePassword;
    }
}
