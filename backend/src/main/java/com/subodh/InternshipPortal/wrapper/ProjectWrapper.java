package com.subodh.InternshipPortal.wrapper;

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
//    private transient MultipartFile projectDescriptionFile;

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
//        this.projectDescriptionFile = "/api/v1/instructors/download?filePath=" + URLEncoder.encode(projectDescriptionFile, StandardCharsets.UTF_8);
        this.projectStatus = projectStatus;


    }


}
