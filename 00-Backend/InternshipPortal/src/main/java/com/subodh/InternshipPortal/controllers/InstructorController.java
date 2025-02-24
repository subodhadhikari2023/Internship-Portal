package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.entities.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/instructorsend")
public class InstructorController {

        @GetMapping("message")
    public ResponseEntity<?> getInstructor() {
        return new ResponseEntity<>(new LoginResponse("Success"), HttpStatus.OK);
    }

}
