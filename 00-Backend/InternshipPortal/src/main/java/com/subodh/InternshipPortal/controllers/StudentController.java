package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.modals.Application;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.*;
import com.subodh.InternshipPortal.wrapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final DepartmentService departmentService;
    private final InternshipStudentsService internshipStudentsService;
    private final ProjectService projectService;
    private final CertificateService certificateService;
    @Value("${file.storage.path}")
    private String rootFolderPath;

    /**
     * Instantiates a new Student controller.
     *
     * @param internshipService  the internship service
     * @param applicationService the application service
     */
    public StudentController(InternshipService internshipService, ApplicationService applicationService, UserService userService, DepartmentService departmentService, InternshipStudentsService internshipStudentsService, ProjectService projectService, CertificateService certificateService) {
        this.internshipService = internshipService;
        this.applicationService = applicationService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.internshipStudentsService = internshipStudentsService;
        this.projectService = projectService;
        this.certificateService = certificateService;
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
     * @param userDetails the user details passed automatically by the spring boot application.
     * @return the response entity with entity as saved of ApplicationWrapper Type
     */
    @PostMapping("apply")
    public ResponseEntity<?> apply(@RequestBody Application application, @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationWrapper saved = applicationService.save(application, userDetails.getUsername());
        return new ResponseEntity<>(new Response<>(saved, "Application Submitted", HttpStatus.CREATED), HttpStatus.CREATED);

    }

    /**
     * Checks whether the student has applied for a particular Internship or not.
     *
     * @param internshipId the internship id
     * @param userDetails  the user details passed automatically by the spring boot application.
     * @return the response entity with entity as true or false
     */
    @GetMapping("has-applied/{internshipId}")
    public ResponseEntity<?> hasApplied(@PathVariable Long internshipId, @AuthenticationPrincipal UserDetails userDetails) {
        Users student = userService.findByUserEmail(userDetails.getUsername());
        Optional<ApplicationWrapper> exists = applicationService.existsByInternshipAndStudent(internshipId, student.getUserId());
        if (exists.isPresent()) {
            return new ResponseEntity<>(new Response<>(true, "Application Exists", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response<>(false, "Message", HttpStatus.OK), HttpStatus.OK);
        }
    }


    @GetMapping("view-submitted-applications")
    public ResponseEntity<?> viewSubmittedApplications(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(new Response<>(applicationService.findAllApplicationsByUserEmail(userDetails.getUsername())), HttpStatus.OK);
    }

    /**
     * Check application status response entity.
     *
     * @param request the request
     * @return the response with ApplicationWrapper object inside it
     */
    @PostMapping("check-application-status")

    public ResponseEntity<?> checkApplicationStatus(@RequestBody APIRequest<Long> request) {
        return new ResponseEntity<>(new Response<>(applicationService.findbyApplicationByApplicationId(request.getEntity())), HttpStatus.OK);

    }

    @GetMapping("departments")
    public ResponseEntity<?> departments() {
        return new ResponseEntity<>(new Response<>(departmentService.findAllDepartments()), HttpStatus.OK);
    }

    @GetMapping("selected-internships")
    public ResponseEntity<?> internship(@AuthenticationPrincipal UserDetails userDetails) {
        List<InternshipStudentsWrapper> all = internshipStudentsService.findAllByStudentId(userService.findByUserEmail(userDetails.getUsername()).getUserId());
        return new ResponseEntity<>(new Response<>(all), HttpStatus.OK);
    }



    @PostMapping("upload-project")
    public ResponseEntity<?> uploadProject(@RequestParam Long projectId, @RequestBody MultipartFile file) {
        ProjectWrapper projectWrapper = projectService.saveProjectFile(projectId, file);
        return new ResponseEntity<>(new Response<>(projectWrapper), HttpStatus.OK);
    }

    @GetMapping("get-profile-details")
    public ResponseEntity<?> getProfileDetails(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return new ResponseEntity<>(new Response<>(new StudentWrapper(user)), HttpStatus.OK);
    }

    @PutMapping("update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody StudentWrapper userWrapper, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Updating user {}", userWrapper);
        return new ResponseEntity<>(new Response<>(userService.updateStudent(userDetails, userWrapper)), HttpStatus.OK);
    }

    @PostMapping("update-profile-picture")
    public ResponseEntity<?> updateProfilePicture(@AuthenticationPrincipal UserDetails userDetails, @RequestPart MultipartFile file) {

        log.info("update-profile-picture");
        return new ResponseEntity<>(userService.updateProfilePicture(userDetails,file),HttpStatus.CREATED);
    }


    @PostMapping("upload-resume")
    public ResponseEntity<?> uploadResume(@AuthenticationPrincipal UserDetails userDetails, @RequestPart MultipartFile file) {
        log.info("uploading resume");
        return new ResponseEntity<>(new Response<>(userService.uploadResume(userDetails,file)),HttpStatus.CREATED);
    }

    @PostMapping("generate-certificate")
    public ResponseEntity<?> generateCertificate(@RequestParam Long internshipStudentId) {
        CertificateWrapper certificate = certificateService.createCertificate(internshipStudentId);
        return new ResponseEntity<>(new Response<>(certificate), HttpStatus.OK);
    }




}
