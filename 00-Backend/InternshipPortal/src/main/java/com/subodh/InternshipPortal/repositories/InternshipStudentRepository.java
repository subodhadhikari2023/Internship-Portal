package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipStudentRepository extends JpaRepository<InternshipStudents, Long> {
    List<InternshipStudents> findAllByStudent(Users student);


    @Query("SELECT i FROM InternshipStudents i LEFT JOIN FETCH i.projects WHERE i.internship.createdBy = :createdBy")
    List<InternshipStudents> findAllByInternship_CreatedBy(@Param("createdBy") Users createdBy);



}
