package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;

import java.util.List;

/**
 * The interface Internship students service.
 */
public interface InternshipStudentsService {
    /**
     * Find all by student id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<InternshipStudentsWrapper> findAllByStudentId(Long userId);

    /**
     * Find all students of internships created list.
     *
     * @param userEmail the user email
     * @return the list
     */
    List<InternshipStudentsWrapper> findAllStudentsOfInternshipsCreated(String userEmail);

}
