package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.dto.Response;
import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.dto.LoginResponse;
import com.subodh.InternshipPortal.repositories.ApplicationRepository;
import com.subodh.InternshipPortal.services.ApplicationService;
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
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;

    public InstructorController(InternshipService internshipService, ApplicationService applicationService, ApplicationRepository applicationRepository) {
        this.internshipService = internshipService;
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("message")
    public ResponseEntity<?> getInstructor() {
        log.info("Endpoint for instructors");
        return new ResponseEntity<>(new LoginResponse("Success"), HttpStatus.OK);
    }

    @PostMapping("internship")
    public ResponseEntity<?> createInternship(@RequestBody Internship internship) {
        Internship savedInternship = internshipService.saveInternship(internship);
        return new ResponseEntity<>(new Response<>(savedInternship, "Internship Created Successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PostMapping("application")
    public ResponseEntity<?> createApplication(@RequestBody Application application) {

        Application saved = applicationService.save(application);
        return new ResponseEntity<>(new Response<>(saved, "Application Created", HttpStatus.CREATED), HttpStatus.CREATED);

    }

}
