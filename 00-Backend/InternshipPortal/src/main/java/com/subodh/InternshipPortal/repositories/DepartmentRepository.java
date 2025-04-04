package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.DepartmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDetails,Long> {
    Optional<DepartmentDetails> findByDepartmentName(String departmentName);
}
