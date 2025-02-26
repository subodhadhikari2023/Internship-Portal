package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import com.subodh.InternshipPortal.wrapper.Response;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.wrapper.LoginResponse;
import com.subodh.InternshipPortal.services.InternshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> createInternship(@RequestBody Internship internship) {
        InternshipWrapper savedInternship = internshipService.saveInternship(internship);
        return new ResponseEntity<>(new Response<>(savedInternship, "Internship Created Successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("internship")
    public ResponseEntity<?> getAllInternship() {
        List<InternshipWrapper> internships = internshipService.findAllByInstructor();
        if (internships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Response<>(internships), HttpStatus.OK);
    }


}
