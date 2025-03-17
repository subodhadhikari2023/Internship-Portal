package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.Project;
import com.subodh.InternshipPortal.wrapper.ProjectWrapper;

public interface ProjectService {
    void createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail);

    Project saveProject(ProjectWrapper project);
}
