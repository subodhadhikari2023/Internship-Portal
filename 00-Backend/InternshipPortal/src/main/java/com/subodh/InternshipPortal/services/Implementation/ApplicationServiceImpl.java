package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.exceptions.ApplicationCreationFailedException;
import com.subodh.InternshipPortal.repositories.ApplicationRepository;
import com.subodh.InternshipPortal.repositories.InternshipRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.ApplicationService;
import com.subodh.InternshipPortal.services.InternshipService;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {
    final
    ApplicationRepository applicationRepository;
    private final UsersRepository usersRepository;
    private final InternshipRepository internshipRepository;
    private final InternshipService internshipService;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, UsersRepository usersRepository, InternshipRepository internshipRepository, InternshipService internshipService) {
        this.applicationRepository = applicationRepository;
        this.usersRepository = usersRepository;
        this.internshipRepository = internshipRepository;
        this.internshipService = internshipService;
    }

    @Override
    public ApplicationWrapper save(Application application) {
        try {

            if (applicationRepository.existsByInternshipAndStudent(application.getInternship(), application.getStudent())) {
                throw new ApplicationCreationFailedException("Student has already applied for this internship.");
            }

            Users student = usersRepository.findByUserId(application.getStudent().getUserId());
            Internship internship = internshipRepository.findByInternshipId(application.getInternship().getInternshipId());
            application.setStatus(StudentApplicationStatus.SUBMITTED);
            application.setStudent(student);
            application.setInternship(internship);
            applicationRepository.save(application);
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
        List<Long> internshipIds = internships.stream().map(InternshipWrapper::getId).toList();

        List<Application> applications = applicationRepository.findByInternshipIds(internshipIds);

        return applications.stream().map(ApplicationWrapper::new).toList();
    }

    @Override
    public ApplicationWrapper reviewApplications(StudentApplicationStatus status, Long applicationId) {
        Optional<Application> application = applicationRepository.findById(applicationId);

        application.get().setStatus(status);

        return new ApplicationWrapper(application.get());
    }


}
