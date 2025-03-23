package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Certificate;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository  extends JpaRepository<Certificate, String> {
    boolean existsByInternshipStudents(InternshipStudents internshipStudents);
}
