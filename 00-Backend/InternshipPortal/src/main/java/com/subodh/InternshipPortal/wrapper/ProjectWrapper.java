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
    private String projectName;
    private String projectDescription;
    private String studentEmail;
    private LocalDate submissionDate;
    private String internshipName;

    public ProjectWrapper(String projectName, String projectDescription, String studentEmail, LocalDate submissionDate) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.studentEmail = studentEmail;
        this.submissionDate = submissionDate;
    }

    public List<ProjectWrapper> fromInternshipStudent(InternshipStudents dbProject) {
        return dbProject.getProjects().stream().map(project ->
                new ProjectWrapper(
                        project.getProjectName(),
                        project.getProjectDescription(),
                        dbProject.getStudent().getUserEmail(),
                        project.getSubmissionDate(),  // Each project retains its own submission date
                        dbProject.getInternship().getInternshipName()
                )
        ).collect(Collectors.toList());
    }
}
