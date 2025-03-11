package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.DepartmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDetails,Long> {
}
