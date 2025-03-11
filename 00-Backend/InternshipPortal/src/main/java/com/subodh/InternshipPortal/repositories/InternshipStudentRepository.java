package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.InternshipStudents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipStudentRepository extends JpaRepository<InternshipStudents, Long> {
}
