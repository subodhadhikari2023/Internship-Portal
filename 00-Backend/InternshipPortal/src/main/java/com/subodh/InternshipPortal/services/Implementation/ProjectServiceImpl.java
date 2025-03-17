package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Project;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.repositories.ProjectRepository;
import com.subodh.InternshipPortal.services.ProjectService;
import com.subodh.InternshipPortal.wrapper.ProjectWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final InternshipStudentRepository internshipStudentRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, InternshipStudentRepository internshipStudentRepository) {
        this.projectRepository = projectRepository;
        this.internshipStudentRepository = internshipStudentRepository;
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
    public Project saveProject(ProjectWrapper project) {
        InternshipStudents studentInternship = internshipStudentRepository.findByInternship_InternshipNameAndStudent_UserEmail(project.getInternshipName(),project.getStudentEmail());
        if (studentInternship == null) {
            throw new RuntimeException("Internship or student not found.");
        }

        Project dbProject = new Project();
        dbProject.setProjectName(project.getProjectName());
        dbProject.setProjectDescription(project.getProjectDescription());
        dbProject.setSubmissionDate(project.getSubmissionDate());
        dbProject.setStatus(StudentInternshipStatus.IN_PROGRESS);
        dbProject.setUser(studentInternship.getStudent());
        studentInternship.getProjects().add(dbProject);

        internshipStudentRepository.save(studentInternship);
       projectRepository.save(dbProject);

       return dbProject;
    }

}
