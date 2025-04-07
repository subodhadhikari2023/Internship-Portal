package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.StudentApplication;
import com.subodh.InternshipPortal.services.*;
import com.subodh.InternshipPortal.wrapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final InternshipStudentsService internshipStudentsService;
    private final ProjectService projectService;
    private final CertificateService certificateService;
    private final UserService userService;

    /**
     * Instantiates a new Instructor controller.
     *
     * @param internshipService         the internship service
     * @param applicationService        the application service
     * @param internshipStudentsService the internship students service
     * @param projectService            the project service
     * @param certificateService        the certificate service
     * @param userService               the user service
     */
    public InstructorController(InternshipService internshipService, ApplicationService applicationService, InternshipStudentsService internshipStudentsService, ProjectService projectService, CertificateService certificateService, UserService userService) {
        this.internshipService = internshipService;
        this.applicationService = applicationService;
        this.internshipStudentsService = internshipStudentsService;
        this.projectService = projectService;
        this.certificateService = certificateService;
        this.userService = userService;
    }


    /**
     * Create internship entity.
     *
     * @param internship  the internship
     * @param userDetails the user details
     * @return the created internship wrapped in the InternshipWrapper
     */
    @PostMapping("internship")
    public ResponseEntity<?> createInternship(@RequestBody Internship internship, @AuthenticationPrincipal UserDetails userDetails) {
        InternshipWrapper savedInternship = internshipService.saveInternship(internship, userDetails.getUsername());
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
        if (internships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(new Response<>(internships), HttpStatus.OK);
    }

    /**
     * Gets number of internship created.
     *
     * @return the number of internship created
     */
    @GetMapping("number-of-internships-created")
    public ResponseEntity<?> getNumberOfInternshipCreated() {
        List<InternshipWrapper> internships = internshipService.findAllByInstructor();
        if (internships.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Response<>(internships.size()), HttpStatus.OK);
    }

    @GetMapping("total-applications-of-internships-created")
    public ResponseEntity<?> getTotalApplicationsOfInternshipCreated(@AuthenticationPrincipal UserDetails userDetails) {
        List<StudentApplication> totalApplications = internshipService.findAllApplicationsbyCreatedBy(userDetails.getUsername());
        if (totalApplications == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Response<>(totalApplications.size()), HttpStatus.OK);
    }

    /**
     * Update internship response entity.
     *
     * @param internship  the internship
     * @param userDetails the user details
     * @return the response entity
     */
    @PutMapping("internship")
    public ResponseEntity<?> updateInternship(@RequestBody Internship internship, @AuthenticationPrincipal UserDetails userDetails) {
        Internship savedInternship = internshipService.findInternshipByInternshipId(internship.getInternshipId());
        log.info("{}", internship.getEducationalQualifications());
        if (savedInternship == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Internship not found");
        }
        savedInternship.setInternshipName(internship.getInternshipName());
        savedInternship.setStartDate(internship.getStartDate());
        savedInternship.setEndDate(internship.getEndDate());
        savedInternship.setDescription(internship.getDescription());
        savedInternship.setEducationalQualifications(internship.getEducationalQualifications());
        savedInternship.setWorkMode(internship.getWorkMode());
        savedInternship.setStatus(internship.getStatus());
        savedInternship.setRequiredSkills(internship.getRequiredSkills());
        internshipService.saveInternship(savedInternship, userDetails.getUsername());
        return ResponseEntity.ok(new InternshipWrapper(savedInternship));
    }


    /**
     * Gets all applications.
     *
     * @return the all applications
     */
    @GetMapping("applications")
    public ResponseEntity<?> getAllApplications() {
        List<ApplicationWrapper> applicationWrapperList = applicationService.findAllofUser();
        return new ResponseEntity<>(new Response<>(applicationWrapperList), HttpStatus.OK);

    }

    /**
     * Review applications response entity.
     *
     * @param status        the status
     * @param applicationId the application id
     * @return the response entity
     */
    @PostMapping("review-applications")
    public ResponseEntity<?> reviewApplications(@RequestBody String status, @RequestParam Long applicationId) {
        return new ResponseEntity<>(new Response<>(applicationService.reviewApplications(StudentApplicationStatus.valueOf(status), applicationId)), HttpStatus.OK);

    }

    /**
     * Gets all internship students.
     *
     * @param userDetails the user details
     * @return the all internship students
     */
    @GetMapping("internship-students")
    public ResponseEntity<?> getAllInternshipStudents(@AuthenticationPrincipal UserDetails userDetails) {
        List<InternshipStudentsWrapper> all = internshipStudentsService.findAllStudentsOfInternshipsCreated(userDetails.getUsername());
        return new ResponseEntity<>(new Response<>(all), HttpStatus.OK);
    }

    /**
     * Create project response entity.
     *
     * @param project the project
     * @param file    the file
     * @return the response entity
     */
    @PostMapping("create-project")
    public ResponseEntity<?> createProject(@RequestPart("project") ProjectWrapper project, @RequestPart("file") MultipartFile file) {
        ProjectWrapper savedProject = projectService.saveProject(project, file);
        return new ResponseEntity<>(new Response<>(savedProject), HttpStatus.CREATED);
    }

    /**
     * Change project status response entity.
     *
     * @param projectId the project id
     * @param status    the status
     * @return the response entity
     */
    @PostMapping("change-project-status/{projectId}")
    public ResponseEntity<?> changeProjectStatus(@PathVariable Long projectId, @RequestBody String status) {
        return new ResponseEntity<>(new Response<>(projectService.changeProjectStatus(projectId, status)), HttpStatus.OK);
    }


    /**
     * Gets student details.
     *
     * @param studentId the student id
     * @return the student details
     */
    @GetMapping("get-student-details")
    public ResponseEntity<?> getStudentDetails(@RequestParam Long studentId) {
        return new ResponseEntity<>(new Response<>(userService.findStudentByStudentId(studentId)), HttpStatus.OK);

    }

    /**
     * Gets profile details.
     *
     * @param userDetails the user details
     * @return the profile details
     */
    @GetMapping("get-profile-details")
    public ResponseEntity<?> getProfileDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(new Response<>(userService.getProfileDetails(userDetails)), HttpStatus.OK);
    }

    /**
     * Update profile picture response entity.
     *
     * @param userDetails the user details
     * @param file        the file
     * @return the response entity
     */
    @PostMapping("update-profile-picture")
    public ResponseEntity<?> updateProfilePicture(@AuthenticationPrincipal UserDetails userDetails, @RequestPart MultipartFile file) {
        log.info("update-profile-picture");
        return new ResponseEntity<>(userService.updateProfilePictureOfInstructors(userDetails, file), HttpStatus.CREATED);
    }

    /**
     * Update profile response entity.
     *
     * @param userDetails       the user details
     * @param instructorWrapper the instructor wrapper
     * @return the response entity
     */
    @PutMapping("update-profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody InstructorWrapper instructorWrapper) {
        return new ResponseEntity<>(new Response<>(userService.updateInstructor(userDetails, instructorWrapper)), HttpStatus.OK);
    }

    /**
     * Generate certificate response entity.
     *
     * @param internshipStudentId the internship student id
     * @return the response entity
     */
    @PostMapping("generate-certificate")
    public ResponseEntity<?> generateCertificate(@RequestParam Long internshipStudentId) {
        CertificateWrapper certificate = certificateService.createCertificate(internshipStudentId);
        return new ResponseEntity<>(new Response<>(certificate), HttpStatus.OK);
    }

    @GetMapping("pending-applications")
    public ResponseEntity<?> getAllPendingApplications() {
        List<ApplicationWrapper> applicationWrapperList = applicationService.findAllofUser();
        return new ResponseEntity<>(new Response<>(applicationWrapperList.size()), HttpStatus.OK);
    }

    @GetMapping("active-internships")
    public ResponseEntity<?> getAllActiveInternships() {
        return new ResponseEntity<>(new Response<>(internshipService.findAllByInstructor_ACTIVE().size()), HttpStatus.OK);
    }

    @GetMapping("recent-internships")
    public ResponseEntity<?> getAllRecentInternships(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(new Response<>(internshipService.findRecentFiveInternships(userDetails.getUsername())), HttpStatus.OK);
    }

    @GetMapping("recent-applications")
    public ResponseEntity<?> getAllRecentApplications(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(new Response<>(applicationService.getAllRecentApplications(userDetails.getUsername())), HttpStatus.OK);
    }

}
