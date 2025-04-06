package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.StudentApplication;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * The interface Student application repository.
 */
public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Long> {
    /**
     * Find all by student list.
     *
     * @param student the student
     * @return the list
     */
    List<StudentApplication> findAllByStudent(Users student);

    /**
     * Find by application id student application.
     *
     * @param applicationId the application id
     * @return the student application
     */
    StudentApplication findByApplicationId(Long applicationId);

    /**
     * Exists by internship and student optional.
     *
     * @param internshipId the internship id
     * @param studentId    the student id
     * @return the optional
     */
    @Query("SELECT sa FROM StudentApplication sa WHERE sa.internship.internshipId = :internshipId AND sa.student.userId = :studentId")
    Optional<ApplicationWrapper> existsByInternshipAndStudent(Long internshipId, Long studentId);


    List<StudentApplication> findAllByInternship_CreatedBy(Users internshipCreatedBy);
}
