package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.exceptions.ApplicationCreationFailedException;
import com.subodh.InternshipPortal.repositories.ApplicationRepository;
import com.subodh.InternshipPortal.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    final
    ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application save(Application application) {
        try {
            return applicationRepository.save(application);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationCreationFailedException("Internship creation failed due to duplicate or invalid data.");
        } catch (IllegalArgumentException e) {
            throw new ApplicationCreationFailedException("Invalid internship data provided.");
        } catch (Exception e) {
            throw new ApplicationCreationFailedException("Unexpected error while creating internship: " + e.getMessage());
        }

    }


}
