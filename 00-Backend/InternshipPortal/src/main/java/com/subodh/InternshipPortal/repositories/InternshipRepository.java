package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    Internship findByInternshipId(Long internshipId);

    @NativeQuery("SELECT * FROM internship where instructors_id = ?")
    List<Internship> findAllByInstructorsId(Long createdBy);

    List<Internship> findAllByCreatedBy(Users createdBy);
}
