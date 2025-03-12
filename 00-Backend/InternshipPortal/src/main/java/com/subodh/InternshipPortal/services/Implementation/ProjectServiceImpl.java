package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.services.ProjectService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
@Async("projectExecutor")
public class ProjectServiceImpl implements ProjectService {
    @Override
    public void createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail) {
        String finalPath = String.format("%s/%s/%s/%s", rootFolderPath, departmentName, internshipName, userEmail);
        File dir = new File(finalPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        CompletableFuture.completedFuture(finalPath);
    }
}
