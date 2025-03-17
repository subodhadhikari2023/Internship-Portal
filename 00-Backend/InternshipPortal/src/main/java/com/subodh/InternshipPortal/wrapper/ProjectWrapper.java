package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.InternshipStudents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public ProjectWrapper(String projectName, String projectDescription, String studentEmail, LocalDate submissionDate,Long projectId) {
        this.projectName = projectName;
        this.projectId=projectId;
        this.projectDescription = projectDescription;
        this.studentEmail = studentEmail;
        this.submissionDate = submissionDate;
    }


}
