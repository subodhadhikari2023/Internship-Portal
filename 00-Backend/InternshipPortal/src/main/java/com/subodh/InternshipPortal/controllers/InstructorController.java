package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.dto.Response;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.dto.LoginResponse;
import com.subodh.InternshipPortal.services.InternshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/instructors")
public class InstructorController {

    private final InternshipService internshipService;

    public InstructorController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @GetMapping("message")
    public ResponseEntity<?> getInstructor() {
        log.info("Endpoint for instructors");
        return new ResponseEntity<>(new LoginResponse("Success"), HttpStatus.OK);
    }

    @PostMapping("internship")
    public ResponseEntity<?> createInternship(@RequestBody Internship internship){
        internshipService.saveInternship(internship);
        return new ResponseEntity<>(new Response("Internship Created Successfully",HttpStatus.CREATED), HttpStatus.CREATED);
    }

}
