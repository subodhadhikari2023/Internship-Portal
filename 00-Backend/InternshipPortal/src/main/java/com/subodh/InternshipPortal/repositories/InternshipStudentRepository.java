package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Internship student repository.
 */
public interface InternshipStudentRepository extends JpaRepository<InternshipStudents, Long> {
    /**
     * Find all by student list.
     *
     * @param student the student
     * @return the list
     */
    List<InternshipStudents> findAllByStudent(Users student);


    /**
     * Find all by internship created by list.
     *
     * @param createdBy the created by
     * @return the list
     */
    @Query("SELECT i FROM InternshipStudents i LEFT JOIN FETCH i.projects WHERE i.internship.createdBy = :createdBy")
    List<InternshipStudents> findAllByInternship_CreatedBy(@Param("createdBy") Users createdBy);


    /**
     * Find by internship internship name and student user email internship students.
     *
     * @param internshipInternshipName the internship internship name
     * @param studentUserEmail         the student user email
     * @return the internship students
     */
    InternshipStudents findByInternship_InternshipNameAndStudent_UserEmail(String internshipInternshipName, String studentUserEmail);

    /**
     * Find all by student user email list.
     *
     * @param studentUserEmail the student user email
     * @return the list
     */
    List<InternshipStudents> findAllByStudent_UserEmail(String studentUserEmail);
}
