package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Project;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.services.ProjectService;
import com.subodh.InternshipPortal.wrapper.ProjectWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service

public class ProjectServiceImpl implements ProjectService {
    private final InternshipStudentRepository internshipStudentRepository;
    @Value("${file.storage.path}")
    private String rootFolderPath;

    public ProjectServiceImpl(InternshipStudentRepository internshipStudentRepository) {
        this.internshipStudentRepository = internshipStudentRepository;
    }

    @Override
    @Async("projectExecutor")
    public CompletableFuture<String> createFolder(String rootFolderPath, String departmentName, String internshipName, String userEmail) {
        String finalPath = String.format("%s/%s/%s/%s", rootFolderPath, departmentName, internshipName, userEmail);
        File dir = new File(finalPath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        return CompletableFuture.completedFuture(finalPath);
    }

    @Override
    public ProjectWrapper saveProject(ProjectWrapper project, MultipartFile file) {
        InternshipStudents studentInternship = internshipStudentRepository.findByInternship_InternshipNameAndStudent_UserEmail(
                project.getInternshipName(), project.getStudentEmail());

        if (studentInternship == null) {
            throw new RuntimeException("Internship or student not found.");
        }

        Project dbProject = new Project();
        dbProject.setProjectName(project.getProjectName());
        dbProject.setProjectDescription(project.getProjectDescription());
        dbProject.setSubmissionDate(project.getSubmissionDate());
        dbProject.setStatus(StudentInternshipStatus.IN_PROGRESS);
        dbProject.setUser(studentInternship.getStudent());

        // ✅ Create folder structure dynamically
        String FILE_PATH = createFolder(rootFolderPath,
                studentInternship.getInternship().getDepartment().getDepartmentName(),
                studentInternship.getInternship().getInternshipName(),
                studentInternship.getStudent().getUserEmail()
        ).join();

        // ✅ Define project description folder path
        String path = String.format("%s/%s/description", FILE_PATH, dbProject.getProjectName());
        File projectFolder = new File(path);

        if (!projectFolder.exists()) {
            projectFolder.mkdirs();  // ✅ Ensure the directory exists
        }

        // ✅ Define description file
        File descriptionFile = new File(projectFolder, "description.pdf");

        try {
            file.transferTo(descriptionFile);  // ✅ Save the file in the correct location
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }

        // ✅ Extract the relative path dynamically
        String absolutePath = descriptionFile.getAbsolutePath();
        String relativePath = absolutePath.replaceFirst("^" + rootFolderPath, "/storage/Internship-Portal");

        // ✅ Store only the relative path in the database
        dbProject.setProjectDescriptionFilePath(relativePath);
        studentInternship.getProjects().add(dbProject);

        // ✅ Save only internshipStudent (Cascade should save the project)
        internshipStudentRepository.save(studentInternship);

        return new ProjectWrapper(
                dbProject.getProjectName(),
                dbProject.getProjectDescription(),
                studentInternship.getStudent().getUserEmail(),
                dbProject.getSubmissionDate(),
                dbProject.getProjectId(),
                dbProject.getProjectDescriptionFilePath()
        );
    }



}
