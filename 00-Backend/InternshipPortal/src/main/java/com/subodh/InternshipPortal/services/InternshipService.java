package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;

import java.util.List;

/**
 * The interface Internship service.
 */
public interface InternshipService {

    /**
     * Save internship internship wrapper.
     *
     * @param internship the internship
     * @param username   the username
     * @return the internship wrapper
     */
    InternshipWrapper saveInternship(Internship internship, String username);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<InternshipWrapper> findAll();

    /**
     * Find all by instructor list.
     *
     * @return the list
     */
    List<InternshipWrapper> findAllByInstructor();

    /**
     * Find internship by internship id internship.
     *
     * @param internshipId the internship id
     * @return the internship
     */
    Internship findInternshipByInternshipId(Long internshipId);
}
