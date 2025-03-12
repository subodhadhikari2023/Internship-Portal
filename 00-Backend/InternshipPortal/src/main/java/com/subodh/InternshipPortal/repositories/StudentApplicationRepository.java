package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.StudentApplication;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Long> {
    List<StudentApplication> findAllByStudent(Users student);

    StudentApplication findByApplicationId(Long applicationId);

    @Query("SELECT sa FROM StudentApplication sa WHERE sa.internship.internshipId = :internshipId AND sa.student.userId = :studentId")
    Optional<ApplicationWrapper> existsByInternshipAndStudent(Long internshipId, Long studentId);

}
