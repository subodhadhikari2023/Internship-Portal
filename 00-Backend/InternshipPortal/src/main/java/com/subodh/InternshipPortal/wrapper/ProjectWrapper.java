package com.subodh.InternshipPortal.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
//    private transient MultipartFile projectDescriptionFile;

    public ProjectWrapper(String projectName,
                          String projectDescription,
                          String studentEmail,
                          LocalDate submissionDate,
                          Long projectId
    ) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectId = projectId;
        this.studentEmail = studentEmail;
        this.submissionDate = submissionDate;


    }




}
