package com.subodh.InternshipPortal.controllers;


import com.subodh.InternshipPortal.entities.LoginResponse;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("api/v1")
public class PublicController {


    private final UserService userService;

    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Users> register(@RequestBody Users user) {
        user.addRole("ROLE_STUDENT");
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
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
