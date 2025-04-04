package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.DepartmentDetails;

import java.util.List;

public interface DepartmentService {
    List<String> findAllDepartments();

    DepartmentDetails createDepartment(String departmentName);
}
