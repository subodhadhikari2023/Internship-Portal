package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.DepartmentDetails;

import java.util.List;

/**
 * The interface Department service.
 */
public interface DepartmentService {
    /**
     * Find all departments list.
     *
     * @return the list
     */
    List<String> findAllDepartments();

    /**
     * Create department department details.
     *
     * @param departmentName the department name
     * @return the department details
     */
    DepartmentDetails createDepartment(String departmentName);
}
