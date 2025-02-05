package com.subodh.InternshipPortal.controllers;



import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
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
    public ResponseEntity<String> login(@RequestBody Users user) {
        String verify = userService.verifyUserCredentials(user);

        return new ResponseEntity<>(verify, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Users>> findAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(),HttpStatus.OK);
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
