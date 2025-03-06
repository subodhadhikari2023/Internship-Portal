package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

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
}
