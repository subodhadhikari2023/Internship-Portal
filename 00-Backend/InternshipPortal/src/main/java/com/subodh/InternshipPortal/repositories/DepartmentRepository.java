package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.DepartmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Department repository.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDetails,Long> {
    /**
     * Find by department name optional.
     *
     * @param departmentName the department name
     * @return the optional
     */
    Optional<DepartmentDetails> findByDepartmentName(String departmentName);
}
