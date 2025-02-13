package com.subodh.InternshipPortal.controllers;


import com.subodh.InternshipPortal.entities.LoginResponse;
import com.subodh.InternshipPortal.entities.OneTimePassword;
import com.subodh.InternshipPortal.entities.RegistrationResponse;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.OTPService;
import com.subodh.InternshipPortal.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

/**
 * The type Public controller.
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("api/v1")
public class PublicController {


    private final UserService userService;
    private final OTPService otpService;
    private final MailService mailService;


    /**
     * Instantiates a new Public controller.
     *
     * @param userService the user service
     * @param otpService  the otp service
     * @param mailSender  the mail sender
     * @param mailService the mail service
     */
    public PublicController(UserService userService, OTPService otpService, JavaMailSenderImpl mailSender, MailService mailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.mailService = mailService;
    }


    /**
     * Register response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping("register")
    public ResponseEntity<?> registerV2(@RequestBody Users user) {
        if (!userService.emailExists(user.getUserEmail())) {
            log.info("Registering user: {}", user);
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
     * @param otp   the otp
     * @param user  the user
     * @return the response entity
     */
    @PostMapping("register/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp, @RequestBody Users user) {

        boolean isValid = otpService.verifyOTP(email, otp);
        if (isValid) {

            user.addRole("ROLE_STUDENT");
            user.setUserEmail(email);
            userService.saveUser(user);
            return ResponseEntity.ok("OTP verified successfully and user has been registered");
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
        String verify = userService.verifyUserCredentials(user);

        log.info("Login endpoint hit for the user {}", user);
        return new ResponseEntity<>(new LoginResponse(verify), HttpStatus.OK);
    }


    /**
     * Home page response entity.
     *
     * @return the response entity
     */
    @GetMapping("home")
    public ResponseEntity<String> homePage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Message after jwt validation string.
     *
     * @return the string
     */
    @GetMapping("message")
    public String messageAfterJwtValidation() {
        return "Jwt token validated";
    }

    /**
     * Students string.
     *
     * @return the string
     */
    @GetMapping("students")
    public String students() {
        return "Hello Students!";
    }

    /**
     * Instructors string.
     *
     * @return the string
     */
    @GetMapping("instructors")
    public String instructors() {
        return "Hello Instructors";
    }

    /**
     * Admin string.
     *
     * @return the string
     */
    @GetMapping("admin")
    public String admin() {
        return "Hello admin";
    }
}
