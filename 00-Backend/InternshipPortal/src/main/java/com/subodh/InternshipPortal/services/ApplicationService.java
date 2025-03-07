package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;

import java.util.List;
import java.util.Optional;

/**
 * The interface Application service.
 */
public interface ApplicationService {
    /**
     * Save application wrapper.
     *
     * @param application the application
     * @param userEmail   the user email
     * @return the application wrapper
     */
    ApplicationWrapper save(Application application, String userEmail);


    /**
     * Find allof user list.
     *
     * @return the list
     */
    List<ApplicationWrapper> findAllofUser();


    /**
     * Review applications application wrapper.
     *
     * @param status        the status
     * @param applicationId the application id
     * @return the application wrapper
     */
    ApplicationWrapper reviewApplications(StudentApplicationStatus status, Long applicationId);

    Optional<ApplicationWrapper> existsByInternshipAndStudent(Long internshipId, Long userId);

    ApplicationWrapper findbyApplicationByApplicationId(Long applicationId);
}
