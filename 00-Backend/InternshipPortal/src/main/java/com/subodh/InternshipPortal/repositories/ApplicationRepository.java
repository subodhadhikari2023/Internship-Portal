package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Application;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The interface Application repository.
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    /**
     * Exists by internship and student optional.
     *
     * @param internshipId the internship id
     * @param studentId    the student id
     * @return the optional
     */
    @Query("SELECT a FROM Application a WHERE a.internship.internshipId IN :internshipId and a.student.userId IN :studentId")
    Optional<ApplicationWrapper> existsByInternshipAndStudent(Long internshipId, Long studentId);


    /**
     * Find by internship ids list.
     *
     * @param internshipIds the internship ids
     * @return the list
     */
    @Query("SELECT a FROM Application a WHERE a.internship.internshipId IN :internshipIds")
    List<Application> findByInternshipIds(@Param("internshipIds") List<Long> internshipIds);


    /**
     * Find by application id application wrapper.
     *
     * @param applicationId the application id
     * @return the application wrapper
     */
    ApplicationWrapper findByApplicationId(Long applicationId);

    /**
     * Find all by student list.
     *
     * @param student the student
     * @return the list
     */
    List<ApplicationWrapper> findAllByStudent(Users student);

//    @Query("SELECT a FROM Application  a WHERE a.student.userId = :userId")
//    List<ApplicationWrapper> findAllByStudentUserId(@Param("userId") Long userId);
}
