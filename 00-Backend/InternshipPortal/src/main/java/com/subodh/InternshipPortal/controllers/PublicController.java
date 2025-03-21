package com.subodh.InternshipPortal.controllers;


import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.wrapper.LoginResponse;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.wrapper.RegistrationResponse;
import com.subodh.InternshipPortal.modals.*;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.OTPService;
import com.subodh.InternshipPortal.services.RegistrationService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * The type Public controller.
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("api/v1/public")
public class PublicController {


    private final UserService userService;
    private final OTPService otpService;
    private final MailService mailService;
    private final RegistrationService registrationService;
    private final UsersRepository usersRepository;


    /**
     * Instantiates a new Public controller.
     *
     * @param userService         the user service
     * @param otpService          the otp service
     * @param mailSender          the mail sender
     * @param mailService         the mail service
     * @param registrationService the registration service
     */
    public PublicController(UserService userService, OTPService otpService, JavaMailSenderImpl mailSender, MailService mailService, RegistrationService registrationService, UsersRepository usersRepository) {
        this.userService = userService;
        this.otpService = otpService;
        this.mailService = mailService;
        this.registrationService = registrationService;
        this.usersRepository = usersRepository;
    }


    /**
     * Register response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping("register")
    @Transactional
    public ResponseEntity<?> registerV2(@RequestBody RegistrationEntity user) {
        if (!userService.emailExists(user.getUserEmail())) {
            log.info("{}", registrationService.findAllByEmail(user.getUserEmail()));
            if (registrationService.findByEmail(user.getUserEmail()) != null) {
                registrationService.delete(user);
            }
            registrationService.save(user);
            OneTimePassword otp = otpService.generateOTP(user);
            mailService.sendMail(user.getUserEmail(), "OTP for Internship Portal", "Dear " + user.getUserEmail() + ",\n your OTP for Internship Portal-Government of Sikkim is \n" + otp.getOneTimePassword());
            String response = "OTP sent: " + otp.getOneTimePassword();
            log.info(response);
            return new ResponseEntity<>(new RegistrationResponse(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(new RegistrationResponse("User already exists"), HttpStatus.CONFLICT);
    }

    /**
     * Verify otp response entity.
     *
     * @param email the email
     * @param otp   the otp              //     * @param user  the user
     * @return the response entity
     */
    @PostMapping("register/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {

        boolean isValid = otpService.verifyOTP(email, otp);
        if (isValid) {
            RegistrationEntity tempUser = registrationService.findByEmail(email);
            userService.saveUser(tempUser);
            return ResponseEntity.ok(new RegistrationResponse("OTP verified successfully and user has been registered"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
        }
    }


    /**
     * Login response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Users user) {

        String verify = null;
        try {
            verify = userService.verifyUserCredentials(user);
            log.info("Login endpoint hit for the user {}", user);
            return new ResponseEntity<>(new LoginResponse(verify), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


    }


    /**
     * Home page response entity.
     *
     * @return the response entity
     */
    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Message after jwt validation string.
     *
     * @return the string
     */
    @GetMapping("message")
    public ResponseEntity<?> messageAfterJwtValidation() {
        return new ResponseEntity<>(new LoginResponse("Valid request"), HttpStatus.OK);
    }


    /**
     * Admin string.
     *
     * @return the string
     */
    @GetMapping("admin")
    public ResponseEntity<?> admin() {
        log.info("Endpoint for admin");
        return new ResponseEntity<>(new LoginResponse("Bearer passed in the header for the admin"), HttpStatus.OK);
    }


}
