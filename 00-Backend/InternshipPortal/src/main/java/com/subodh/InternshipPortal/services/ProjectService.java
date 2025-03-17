package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.modals.Project;

public interface ProjectService {
    void createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail);

    Project saveProject(Project project);
}
