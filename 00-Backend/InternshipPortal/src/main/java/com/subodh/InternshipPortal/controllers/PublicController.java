package com.subodh.InternshipPortal.controllers;


import com.subodh.InternshipPortal.entities.LoginResponse;
import com.subodh.InternshipPortal.entities.OneTimePassword;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.OTPService;
import com.subodh.InternshipPortal.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("api/v1")
public class PublicController {


    private final UserService userService;
    private final OTPService otpService;
    private final UsersRepository usersRepository;
    private final MailService mailService;


    public PublicController(UserService userService, OTPService otpService, UsersRepository usersRepository, JavaMailSenderImpl mailSender, MailService mailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.usersRepository = usersRepository;
        this.mailService = mailService;
    }



    @PostMapping("register")
    public ResponseEntity<String> registerV2(@RequestBody Users user) {
        OneTimePassword otp = otpService.generateOTP(user);
        mailService.sendMail(user.getUserEmail(), "OTP for Internship Portal", "Dear " + user.getUserEmail() + ", your OTP for Internship Portal-Government of Sikkim is \n" + otp.getOneTimePassword());
        return ResponseEntity.ok("OTP sent: " + otp.getOneTimePassword());

    }

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


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        String verify = userService.verifyUserCredentials(user);

        log.info("Login endpoint hit for the user {}", user);
        return new ResponseEntity<>(new LoginResponse(verify), HttpStatus.OK);
    }


    @GetMapping("home")
    public ResponseEntity<String> homePage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("message")
    public String messageAfterJwtValidation() {
        return "Jwt token validated";
    }

    @GetMapping("students")
    public String students() {
        return "Hello Students!";
    }

    @GetMapping("instructors")
    public String instructors() {
        return "Hello Instructors";
    }

    @GetMapping("admin")
    public String admin() {
        return "Hello admin";
    }
}
