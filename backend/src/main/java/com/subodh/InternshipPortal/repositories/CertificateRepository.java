package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Certificate;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Certificate repository.
 */
@Repository
public interface CertificateRepository  extends JpaRepository<Certificate, String> {
    /**
     * Exists by internship students boolean.
     *
     * @param internshipStudents the internship students
     * @return the boolean
     */
    boolean existsByInternshipStudents(InternshipStudents internshipStudents);
}
