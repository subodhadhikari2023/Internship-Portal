package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.modals.Project;
import com.subodh.InternshipPortal.repositories.ProjectRepository;
import com.subodh.InternshipPortal.services.ProjectService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Async("projectExecutor")
    public void createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail) {
        String finalPath = String.format("%s/%s/%s/%s", rootFolderPath, departmentName, internshipName, userEmail);
        File dir = new File(finalPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        CompletableFuture.completedFuture(finalPath);
    }

    @Override
    public Project saveProject(Project project) {
      return projectRepository.save(project);
    }
}
