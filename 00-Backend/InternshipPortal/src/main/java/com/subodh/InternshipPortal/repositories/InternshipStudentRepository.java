package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipStudentRepository extends JpaRepository<InternshipStudents, Long> {
    List<InternshipStudents> findAllByStudent(Users student);

    List<InternshipStudents> findAllByInternship_CreatedBy(Users internshipCreatedBy);
}
