package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.*;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.exceptions.ApplicationCreationFailedException;
import com.subodh.InternshipPortal.repositories.*;
import com.subodh.InternshipPortal.services.ApplicationService;
import com.subodh.InternshipPortal.services.InternshipService;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.ProjectService;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The type Application service.
 */
@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {
    /**
     * The Application repository.
     */
    private final ApplicationRepository applicationRepository;
    private final UsersRepository usersRepository;
    private final InternshipRepository internshipRepository;
    private final InternshipService internshipService;
    private final InternshipStudentRepository internshipStudentRepository;
    private final MailService mailService;
    private final StudentApplicationRepository studentApplicationRepository;
    private final ProjectService projectService;
    @Value("${file.storage.path}")
    private String rootFolderPath;

    /**
     * Instantiates a new Application service.
     *
     * @param applicationRepository the application repository
     * @param usersRepository       the users repository
     * @param internshipRepository  the internship repository
     * @param internshipService     the internship service
     */
    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, UsersRepository usersRepository, InternshipRepository internshipRepository, InternshipService internshipService, InternshipStudentRepository internshipStudentRepository, MailService mailService, StudentApplicationRepository studentApplicationRepository, ProjectService projectService) {
        this.applicationRepository = applicationRepository;
        this.usersRepository = usersRepository;
        this.internshipRepository = internshipRepository;
        this.internshipService = internshipService;
        this.internshipStudentRepository = internshipStudentRepository;
        this.mailService = mailService;
        this.studentApplicationRepository = studentApplicationRepository;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public ApplicationWrapper save(Application application, String userEmail) {
        try {

            Users student = usersRepository.findByUserEmail(userEmail);
            Optional<?> exists = applicationRepository.existsByInternshipAndStudent(application.getInternship().getInternshipId(), student.getUserId());
            if (exists.isPresent()) {
                throw new ApplicationCreationFailedException("Student has already applied for this internship.");
            }

            Internship internship = internshipRepository.findByInternshipId(application.getInternship().getInternshipId());
            application.setStatus(StudentApplicationStatus.SUBMITTED);
            application.setStudent(student);
            application.setInternship(internship);
            applicationRepository.save(application);
            studentApplicationRepository.save(new StudentApplication(application));

            return new ApplicationWrapper(application);

        } catch (IllegalArgumentException e) {
            throw new ApplicationCreationFailedException("Invalid Internship Data Provided: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationCreationFailedException("Application creation failed due to duplicate or invalid data.");
        } catch (Exception e) {
            throw new ApplicationCreationFailedException("Unexpected error while creating application: " + e.getMessage());
        }
    }

    @Override
    public List<ApplicationWrapper> findAllofUser() {
        List<InternshipWrapper> internships = internshipService.findAllByInstructor();
        List<Long> internshipIds = internships.stream().map(InternshipWrapper::getInternshipId).toList();
        List<Application> applications = applicationRepository.findByInternshipIds(internshipIds);
        return applications.stream().map(ApplicationWrapper::new).toList();
    }


    @Override
    public ApplicationWrapper reviewApplications(StudentApplicationStatus status, Long applicationId) {
        Application application = applicationRepository.findById(applicationId).get();
        if (status == StudentApplicationStatus.ACCEPTED) {
            InternshipStudents student = new InternshipStudents();
            student.setInternship(application.getInternship());
            student.setStudent(application.getStudent());
            student.setStatus(StudentInternshipStatus.valueOf("IN_PROGRESS"));
            internshipStudentRepository.save(student);
            projectService.createFolder(rootFolderPath, application.getInternship().getDepartment().getDepartmentName(), application.getInternship().getInternshipName(), application.getStudent().getUserEmail());


        }
        StudentApplication studentApplication = studentApplicationRepository.findByApplicationId(applicationId);
        studentApplication.setStatus(status);
        StudentApplication saved = studentApplicationRepository.save(studentApplication);
        applicationRepository.delete(application);
        mailService.sendApplicationStatusMail(application.getStudent().getUserEmail(), application.getStudent().getUserEmail(), application.getInternship().getInternshipName(), status, application.getInternship().getCreatedBy().getUserEmail(), application.getInternship().getCreatedBy().getDepartment().getDepartmentName());
        return new ApplicationWrapper(saved);
    }

    @Override
    public Optional<ApplicationWrapper> existsByInternshipAndStudent(Long internshipId, Long userId) {
        return applicationRepository.existsByInternshipAndStudent(internshipId, userId);
    }

    @Override
    public ApplicationWrapper findbyApplicationByApplicationId(Long applicationId) {
        return applicationRepository.findByApplicationId(applicationId);
    }

    @Override
    public List<ApplicationWrapper> findAllApplicationsByUserEmail(String username) {
        Users user = usersRepository.findByUserEmail(username);
        return studentApplicationRepository.findAllByStudent(user).stream().map(ApplicationWrapper::new).toList();
    }


}
