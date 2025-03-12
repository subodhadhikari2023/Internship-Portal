package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.InternshipStudents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternshipStudentsWrapper {
    private String internshipName;
    private String userEmail;
    private String projectName;

    public InternshipStudentsWrapper(InternshipStudents internshipStudents) {
        this.internshipName = internshipStudents.getInternship().getInternshipName();
        this.userEmail = internshipStudents.getStudent().getUserEmail();
        //To be done
        this.projectName = "";
    }
}
