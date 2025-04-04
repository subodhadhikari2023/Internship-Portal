package com.subodh.InternshipPortal.controllers;


import com.subodh.InternshipPortal.exceptions.InvalidJWTTokenException;
import com.subodh.InternshipPortal.wrapper.*;
import com.subodh.InternshipPortal.modals.*;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.OTPService;
import com.subodh.InternshipPortal.services.RegistrationService;
import com.subodh.InternshipPortal.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;

/**
 * The type Public controller.
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/v1/public")
public class PublicController {


    private final UserService userService;
    private final OTPService otpService;
    private final MailService mailService;
    private final RegistrationService registrationService;
    @Value("${file.storage.path}")
    private String rootFolderPath;


    /**
     * Instantiates a new Public controller.
     *
     * @param userService         the user service
     * @param otpService          the otp service
     * @param mailService         the mail service
     * @param registrationService the registration service
     */
    public PublicController(UserService userService, OTPService otpService, MailService mailService, RegistrationService registrationService) {
        this.userService = userService;
        this.otpService = otpService;
        this.mailService = mailService;
        this.registrationService = registrationService;
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
            if (registrationService.findByEmail(user.getUserEmail()) != null) {
                registrationService.delete(user);
            }
            registrationService.save(user);
            OneTimePassword otp = otpService.generateOTP(user);
            mailService.sendMail(user.getUserEmail(), "OTP for Internship Portal", "Dear " + user.getUserEmail() + ",\n your OTP for Internship Portal-Government of Sikkim is \n" + otp.getOneTimePassword());
            String response = "OTP sent: " + otp.getOneTimePassword();
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
            return new ResponseEntity<>(new LoginResponse(verify), HttpStatus.OK);
        } catch (Exception e) {
            throw new InvalidJWTTokenException("Invalid JWT token");
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


    @GetMapping("download")
    public ResponseEntity<Resource> downloadFile(String filePath) {
        assert filePath != null;
        log.info("Downloading file: {}", filePath);
        try {
            String absolutePath = rootFolderPath + filePath.replace("/storage/Internship-Portal", "");

            File file = new File(absolutePath);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(file.toURI());

            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            if (contentType.startsWith("image/")) {
                headers.setContentType(MediaType.parseMediaType(contentType));
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());
            }
            log.info(resource.getFilename());

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        return new ResponseEntity<>(new Response<>(userService.forgetPassword(email)), HttpStatus.OK);
    }

    @GetMapping("get-password-change-otp")
    public ResponseEntity<?> getPasswordChangeOtp(@RequestParam String email) {
        OneTimePassword oneTimePassword = otpService.generateOTPForPasswordReset(email);
        return new ResponseEntity<>(new Response<>(oneTimePassword), HttpStatus.OK);
    }

    @PostMapping("validate-otp")
    public ResponseEntity<?> resetPassword(@RequestBody OneTimePassword oneTimePassword) {
        log.info("OTP: {}", oneTimePassword.getOneTimePassword());
        return otpService.verifyOTP(oneTimePassword.getUserEmail(), oneTimePassword.getOneTimePassword()) ? new ResponseEntity<>(new Response<>("OTP verified successfully"), HttpStatus.OK) : new ResponseEntity<>(new Response<>("Invalid OTP"), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String password) {
        UserWrapper userWrapper = userService.resetPasswordUserFoundByEmail(email, password);
        if (userWrapper != null) {
            return new ResponseEntity<>(new Response<>(userWrapper), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

    }

}
