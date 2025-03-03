package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.services.ApplicationService;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import com.subodh.InternshipPortal.wrapper.Response;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.services.InternshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Instructor controller.
 */
@Slf4j
@RestController
@RequestMapping("api/v1/instructors")
@CrossOrigin
public class InstructorController {

    private final InternshipService internshipService;
    private final ApplicationService applicationService;


    /**
     * Instantiates a new Instructor controller.
     *
     * @param internshipService the internship service
     */
    public InstructorController(InternshipService internshipService, ApplicationService applicationService) {
        this.internshipService = internshipService;
        this.applicationService = applicationService;
    }


    /**
     * Create internship entity.
     *
     * @param internship the internship
     * @return the created internship wrapped in the InternshipWrapper
     */
    @PostMapping("internship")
    public ResponseEntity<?> createInternship(@RequestBody Internship internship, @AuthenticationPrincipal UserDetails userDetails) {
        InternshipWrapper savedInternship = internshipService.saveInternship(internship,userDetails.getUsername());
        return new ResponseEntity<>(new Response<>(savedInternship, "Internship Created Successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    /**
     * Gets all internship.
     *
     * @return the all internship
     */
    @GetMapping("internship")
    public ResponseEntity<?> getAllInternship() {
        List<InternshipWrapper> internships = internshipService.findAllByInstructor();
        log.info("All internships found");
        if (internships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Response<>(internships), HttpStatus.OK);
    }

    @GetMapping("applications")
    public ResponseEntity<?> getAllApplications() {
        List<ApplicationWrapper> applicationWrapperList = applicationService.findAllofUser();
        return new ResponseEntity<>(new Response<>(applicationWrapperList), HttpStatus.OK);

    }
    @PostMapping("review-applications")
    public ResponseEntity<?> reviewApplications(@RequestBody StudentApplicationStatus status, @RequestParam Long applicationId) {
        return  new ResponseEntity<>(new Response<>(applicationService.reviewApplications(status,applicationId),"Application status updated",HttpStatus.ACCEPTED),HttpStatus.OK);

    }



}
