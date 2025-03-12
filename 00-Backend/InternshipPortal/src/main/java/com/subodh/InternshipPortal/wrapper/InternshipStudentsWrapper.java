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

import static org.yaml.snakeyaml.tokens.Token.ID.Value;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternshipStudentsWrapper {
    private String internshipName;
    private String userEmail;
    private Set<String> projectName;
    private String instructorName;
    private String departmentName;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String workMode;


    public InternshipStudentsWrapper(InternshipStudents internshipStudents) {
        this.internshipName = internshipStudents.getInternship().getInternshipName();
        this.userEmail = internshipStudents.getStudent().getUserEmail();
        this.projectName = internshipStudents.getProjects().stream().map(Project::getProjectName).collect(Collectors.toSet());
        this.instructorName = internshipStudents.getInternship().getCreatedBy().getUserEmail();
        this.departmentName = internshipStudents.getInternship().getDepartment().getDepartmentName();
        this.status = String.valueOf(internshipStudents.getInternship().getStatus());
        this.startDate = internshipStudents.getInternship().getStartDate();
        this.endDate = internshipStudents.getInternship().getEndDate();
        this.workMode = String.valueOf(internshipStudents.getInternship().getWorkMode());


    }
}
