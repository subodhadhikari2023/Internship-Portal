package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.ProjectWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ProjectService {
    CompletableFuture<String> createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail);

    ProjectWrapper saveProject(ProjectWrapper project, MultipartFile file);
}
