package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The type Project wrapper.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWrapper {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private String studentEmail;
    private LocalDate submissionDate;
    private String internshipName;
    private String projectDescriptionFile;
    private String projectFile;
    private String projectStatus;
    private String department;

    /**
     * Instantiates a new Project wrapper.
     *
     * @param projectName            the project name
     * @param projectDescription     the project description
     * @param studentEmail           the student email
     * @param submissionDate         the submission date
     * @param projectId              the project id
     * @param projectDescriptionFile the project description file
     * @param projectFile            the project file
     * @param projectStatus          the project status
     */
    public ProjectWrapper(String projectName,
                          String projectDescription,
                          String studentEmail,
                          LocalDate submissionDate,
                          Long projectId,
                          String projectDescriptionFile,
                          String projectFile,
                          String projectStatus

    ) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectId = projectId;
        this.studentEmail = studentEmail;
        this.submissionDate = submissionDate;
        this.projectFile = projectFile;
        this.projectDescriptionFile = projectDescriptionFile;
        this.projectStatus = projectStatus;


    }


    public ProjectWrapper(Project project) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.projectDescription = project.getProjectDescription();
        this.projectDescriptionFile = project.getProjectDescriptionFilePath();
        this.projectFile = project.getProjectFile();
        this.studentEmail=project.getUser().getUserEmail();
        if (project.getInternshipStudents() != null && project.getInternshipStudents().getInternship() != null) {
            this.internshipName = project.getInternshipStudents().getInternship().getInternshipName();
        } else {
            this.internshipName = null;
        }
         this.department=project.getInternshipStudents().getInternship().getDepartment().getDepartmentName();


    }
}
