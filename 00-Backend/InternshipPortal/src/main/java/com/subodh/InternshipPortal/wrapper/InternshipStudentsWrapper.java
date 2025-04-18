package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Internship students wrapper.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternshipStudentsWrapper {
    private Long internshipStudentId;
    private String internshipName;
    private String userEmail;
    private Set<ProjectWrapper> projects;
    private String instructorName;
    private String departmentName;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String workMode;
    private String certificateFilePath;


    /**
     * Instantiates a new Internship students wrapper.
     *
     * @param internshipStudents the internship students
     */
    public InternshipStudentsWrapper(InternshipStudents internshipStudents) {
        Set<Project> projectsCopy = Set.copyOf(internshipStudents.getProjects());
        this.internshipStudentId = internshipStudents.getStudentInternshipId();
        this.internshipName = internshipStudents.getInternship().getInternshipName();
        this.userEmail = internshipStudents.getStudent().getUserEmail();
        this.projects = projectsCopy.stream()
                .map(project -> new ProjectWrapper(
                        project.getProjectId(),
                        project.getProjectName(),
                        project.getProjectDescription(),
                        internshipStudents.getStudent().getUserEmail(),
                        project.getSubmissionDate(),
                        internshipStudents.getInternship().getInternshipName(),
                        project.getProjectDescriptionFilePath(),
                        project.getProjectFile(),
                        String.valueOf(project.getStatus())

                ))
                .collect(Collectors.toSet());
        this.instructorName = internshipStudents.getInternship().getCreatedBy().getUserEmail();
        this.departmentName = internshipStudents.getInternship().getDepartment().getDepartmentName();
        this.status = String.valueOf(internshipStudents.getStatus());
        this.startDate = internshipStudents.getInternship().getStartDate();
        this.endDate = internshipStudents.getInternship().getEndDate();
        this.workMode = String.valueOf(internshipStudents.getInternship().getWorkMode());
        this.certificateFilePath =
                (internshipStudents.getCertificate() != null) ? internshipStudents.getCertificate().getCertificateFilePath() : "";

    }
}
