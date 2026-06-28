package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.ProjectWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

/**
 * The interface Project service.
 */
public interface ProjectService {
    /**
     * Create folder completable future.
     *
     * @param rootFolderPath the root folder path
     * @param departmentName the department name
     * @param internshipName the internship name
     * @param userEmail      the user email
     * @return the completable future
     */
    CompletableFuture<String> createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail);

    /**
     * Save project project wrapper.
     *
     * @param project the project
     * @param file    the file
     * @return the project wrapper
     */
    ProjectWrapper saveProject(ProjectWrapper project, MultipartFile file);

    /**
     * Save project file project wrapper.
     *
     * @param projectId the project id
     * @param file      the file
     * @return the project wrapper
     */
    ProjectWrapper saveProjectFile(Long projectId, MultipartFile file);

    /**
     * Change project status project wrapper.
     *
     * @param projectId the project id
     * @param status    the status
     * @return the project wrapper
     */
    ProjectWrapper changeProjectStatus(Long projectId, String status);
}
