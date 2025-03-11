package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Application;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * The class used to wrap and send response Application.
 */
@AllArgsConstructor
@Builder
@Data
public class ApplicationWrapper {

    /**
     * Instantiates a new Application wrapper.
     *
     * @param application the application
     */
    public ApplicationWrapper(Application application) {
        this.internshipTitle = application.getInternship().getInternshipName();
        this.status = application.getStatus();
        this.contactPerson = application.getInternship().getCreatedBy().getUserEmail();
        this.applicationId = application.getApplicationId();
        this.applicantName = application.getStudent().getUserName();
        this.applicantEmail = application.getStudent().getUserEmail();
        this.studentSkills = application.getStudent().getStudentSkills();
    }

    private String internshipTitle;
    private StudentApplicationStatus status;
    private String contactPerson;
    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    Set<String> studentSkills;

}
