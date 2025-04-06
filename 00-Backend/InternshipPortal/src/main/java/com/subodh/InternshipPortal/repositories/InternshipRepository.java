package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;

/**
 * The interface Internship repository.
 */
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    /**
     * Find by internship id internship.
     *
     * @param internshipId the internship id
     * @return the internship
     */
    Internship findByInternshipId(Long internshipId);

    /**
     * Find all by instructors id list.
     *
     * @param createdBy the created by
     * @return the list
     */
    @NativeQuery("SELECT * FROM internship where instructors_id = ?")
    List<Internship> findAllByInstructorsId(Long createdBy);

    /**
     * Find all by created by list.
     *
     * @param createdBy the created by
     * @return the list
     */
    List<Internship> findAllByCreatedBy(Users createdBy);

    List<Internship> findAllByStatusAndCreatedBy(InternshipStatus status, Users createdBy);

    List<Internship> findTop5ByCreatedByOrderByStartDateDesc(Users createdBy);
}
