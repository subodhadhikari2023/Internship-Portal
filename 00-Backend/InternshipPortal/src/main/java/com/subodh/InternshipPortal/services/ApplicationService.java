package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.Application;
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

    /**
     * Exists by internship and student optional.
     *
     * @param internshipId the internship id
     * @param userId       the user id
     * @return the optional
     */
    Optional<ApplicationWrapper> existsByInternshipAndStudent(Long internshipId, Long userId);

    /**
     * Findby application by application id application wrapper.
     *
     * @param applicationId the application id
     * @return the application wrapper
     */
    ApplicationWrapper findbyApplicationByApplicationId(Long applicationId);

    /**
     * Find all applications by user email list.
     *
     * @param username the username
     * @return the list
     */
    List<ApplicationWrapper> findAllApplicationsByUserEmail(String username);

    List<ApplicationWrapper> getAllRecentApplications(String userEmail);
}
