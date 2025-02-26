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
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    final
    ApplicationRepository applicationRepository;
    private final UsersRepository usersRepository;
    private final InternshipRepository internshipRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, UsersRepository usersRepository, InternshipRepository internshipRepository) {
        this.applicationRepository = applicationRepository;
        this.usersRepository = usersRepository;
        this.internshipRepository = internshipRepository;
    }

    @Override
    public ApplicationWrapper save(Application application) {
        try {

            if (applicationRepository.existsByInternshipAndStudent(application.getInternship(), application.getStudent())) {
                throw new ApplicationCreationFailedException("Student has already applied for this internship.");
            }

            Users student = usersRepository.findByUserEmail(application.getStudent().getUserEmail());
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


}
