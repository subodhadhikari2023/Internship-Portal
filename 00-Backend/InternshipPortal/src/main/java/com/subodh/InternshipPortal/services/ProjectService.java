package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.ProjectWrapper;

public interface ProjectService {
    void createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail);

    ProjectWrapper saveProject(ProjectWrapper project);
}
