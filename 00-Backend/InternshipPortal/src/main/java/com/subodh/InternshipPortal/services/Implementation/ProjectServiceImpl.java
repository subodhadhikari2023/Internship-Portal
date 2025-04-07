package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Project;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.repositories.ProjectRepository;
import com.subodh.InternshipPortal.services.MailService;
import com.subodh.InternshipPortal.services.ProjectService;
import com.subodh.InternshipPortal.wrapper.ProjectWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The type Project service.
 */
@Slf4j
@Service

public class ProjectServiceImpl implements ProjectService {
    private final InternshipStudentRepository internshipStudentRepository;
    private final ProjectRepository projectRepository;
    private final MailService mailService;
    @Value("${file.storage.path}")
    private String rootFolderPath;

    /**
     * Instantiates a new Project service.
     *
     * @param internshipStudentRepository the internship student repository
     * @param projectRepository           the project repository
     */
    public ProjectServiceImpl(InternshipStudentRepository internshipStudentRepository, ProjectRepository projectRepository, MailService mailService) {
        this.internshipStudentRepository = internshipStudentRepository;
        this.projectRepository = projectRepository;
        this.mailService = mailService;
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
        InternshipStudents studentInternship = internshipStudentRepository.findByInternship_InternshipNameAndStudent_UserEmail(project.getInternshipName(), project.getStudentEmail());

        if (studentInternship == null) {
            throw new RuntimeException("Internship or student not found.");
        }

        Project dbProject = new Project();
        dbProject.setProjectName(project.getProjectName());
        dbProject.setProjectDescription(project.getProjectDescription());
        dbProject.setSubmissionDate(project.getSubmissionDate());
        dbProject.setStatus(StudentInternshipStatus.IN_PROGRESS);
        dbProject.setUser(studentInternship.getStudent());

        String FILE_PATH = createFolder(rootFolderPath, studentInternship.getInternship().getDepartment().getDepartmentName(), studentInternship.getInternship().getInternshipName(), studentInternship.getStudent().getUserEmail()).join();

        String path = String.format("%s/%s/description", FILE_PATH, dbProject.getProjectName());
        File projectFolder = new File(path);

        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }

        File descriptionFile = new File(projectFolder, "description.pdf");

        try {
            file.transferTo(descriptionFile);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }

        String absolutePath = descriptionFile.getAbsolutePath();
        String relativePath = absolutePath.replaceFirst("^" + rootFolderPath, "/storage/Internship-Portal");

        dbProject.setProjectDescriptionFilePath(relativePath);
        studentInternship.getProjects().add(dbProject);
        mailService.sendProjectAllocationMail(studentInternship.getStudent().getUserName(),dbProject.getProjectName(),dbProject.getSubmissionDate());
        internshipStudentRepository.save(studentInternship);

        return new ProjectWrapper(dbProject.getProjectName(), dbProject.getProjectDescription(), studentInternship.getStudent().getUserEmail(), dbProject.getSubmissionDate(), dbProject.getProjectId(), dbProject.getProjectDescriptionFilePath(), dbProject.getProjectFile(), String.valueOf(dbProject.getStatus()));
    }

    @Override
    public ProjectWrapper saveProjectFile(Long projectId, MultipartFile file) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        File submissionFile = getSubmissionFile(file, project);

        try {
            file.transferTo(submissionFile);
        } catch (IOException e) {
            throw new RuntimeException("Error saving project file: " + e.getMessage(), e);
        }

        String absolutePath = submissionFile.getAbsolutePath();
        String relativePath = absolutePath.replaceFirst("^" + rootFolderPath, "/storage/Internship-Portal");

        project.setProjectFile(relativePath);
        projectRepository.save(project);

        return new ProjectWrapper(
                project.getProjectName(),
                project.getProjectDescription(),
                project.getUser().getUserEmail(),
                project.getSubmissionDate(),
                project.getProjectId(),
                project.getProjectDescriptionFilePath(),
                project.getProjectFile(),
                String.valueOf(project.getStatus()));
    }

    private File getSubmissionFile(MultipartFile file, Project project) {
        String descriptionFilePath = project.getProjectDescriptionFilePath();
        if (descriptionFilePath == null || descriptionFilePath.isBlank()) {
            throw new RuntimeException("Project description file path not found.");
        }


        String projectFolderPath = descriptionFilePath
                .replace("/storage/Internship-Portal", "") // Convert to relative path
                .replace("/description/description.pdf", ""); // Properly trim description file

        return getSubmissionFile(file, projectFolderPath);
    }

    private File getSubmissionFile(MultipartFile file, String projectFolderPath) {
        File projectFolder = new File(rootFolderPath, projectFolderPath);

        if (!projectFolder.exists()) {
            throw new RuntimeException("Project folder does not exist.");
        }

        File projectFilesFolder = new File(projectFolder, "project-files");
        if (!projectFilesFolder.exists()) {
            projectFilesFolder.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newFilename = "projectData" + fileExtension;

        return new File(projectFilesFolder, newFilename);
    }

    @Override
    public ProjectWrapper changeProjectStatus(Long projectId, String status) {
        Optional<Project> project = Optional.ofNullable(projectRepository.findById(projectId).orElse(null));
        Project dbProject = project.get();
        dbProject.setStatus(StudentInternshipStatus.valueOf(status));
        projectRepository.save(dbProject);


        String userEmail = project.get().getUser().getUserEmail();
        List<InternshipStudents> internshipStudentsList = internshipStudentRepository.findAllByStudent_UserEmail(userEmail);

        for (InternshipStudents internshipStudents : internshipStudentsList) {
            boolean allProjectsCompleted = internshipStudents.getProjects().stream().allMatch(p -> p.getStatus().equals(StudentInternshipStatus.COMPLETED));
            if (allProjectsCompleted) {
                internshipStudents.setStatus(StudentInternshipStatus.COMPLETED);
                internshipStudentRepository.save(internshipStudents);
            }
        }

        mailService.sendProjectStatusChangeMail(userEmail,dbProject.getProjectName(),dbProject.getStatus(), LocalDate.now());

        return new ProjectWrapper(
                dbProject.getProjectName(),
                dbProject.getProjectDescription(),
                dbProject.getUser().getUserEmail(),
                dbProject.getSubmissionDate(),
                dbProject.getProjectId(),
                dbProject.getProjectDescriptionFilePath(),
                dbProject.getProjectFile(),
                String.valueOf(Objects.requireNonNull(projectRepository.findById(projectId).orElse(null)).getStatus())
        );
    }

}
