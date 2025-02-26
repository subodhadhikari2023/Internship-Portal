package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    Internship findByInternshipId(Long internshipId);
}
