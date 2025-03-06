package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.services.ApplicationService;
import com.subodh.InternshipPortal.services.InternshipService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import com.subodh.InternshipPortal.wrapper.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The type Student controller.
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/v1/students")
public class StudentController {

    private final InternshipService internshipService;
    private final ApplicationService applicationService;
    private final UserService userService;

    /**
     * Instantiates a new Student controller.
     *
     * @param internshipService  the internship service
     * @param applicationService the application service
     */
    public StudentController(InternshipService internshipService, ApplicationService applicationService, UserService userService) {
        this.internshipService = internshipService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    /**
     * View internships response entity.
     *
     * @return the response entity
     */
    @GetMapping("view-internships")
    public ResponseEntity<?> viewInternships() {
        List<InternshipWrapper> internshipList = internshipService.findAll();
        return new ResponseEntity<>(new Response<>(internshipList), HttpStatus.OK);
    }

    /**
     * Apply response entity.
     *
     * @param application the application
     * @param userDetails the user details
     * @return the response entity
     */
    @PostMapping("apply")
    public ResponseEntity<?> apply(@RequestBody Application application, @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationWrapper saved = applicationService.save(application, userDetails.getUsername());
        return new ResponseEntity<>(new Response<>(saved, "Application Submitted", HttpStatus.CREATED), HttpStatus.CREATED);

    }

    @GetMapping("/has-applied/{internshipId}")
    public ResponseEntity<?> hasApplied(@PathVariable Long internshipId, @AuthenticationPrincipal UserDetails userDetails) {
        Users student = userService.findByUserEmail(userDetails.getUsername());
        Optional<ApplicationWrapper> exists = applicationService.existsByInternshipAndStudent(internshipId, student.getUserId());
        if (exists.isPresent()) {
            return new ResponseEntity<>(new Response<>(true, "Application Exists", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response<>(false, "Message", HttpStatus.OK), HttpStatus.OK);
        }
    }

}
