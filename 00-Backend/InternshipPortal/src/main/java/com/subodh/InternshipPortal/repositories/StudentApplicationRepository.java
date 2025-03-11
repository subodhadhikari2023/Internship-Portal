package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.StudentApplication;
import com.subodh.InternshipPortal.modals.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Long> {
    List<StudentApplication> findAllByStudent(Users student);

    StudentApplication findByApplicationId(Long applicationId);
}
