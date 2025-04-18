package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Application;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.modals.StudentApplication;
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
        this.studentId = application.getStudent().getUserId();
        this.applicantEmail = application.getStudent().getUserEmail();
        this.studentSkills = application.getStudent().getStudentSkills();
    }

    /**
     * Instantiates a new Application wrapper.
     *
     * @param application the application
     */
    public ApplicationWrapper(StudentApplication application) {
        this.internshipTitle = application.getInternship().getInternshipName();
        this.status = application.getStatus();
        this.contactPerson = application.getInternship().getCreatedBy().getUserEmail();
        this.applicationId = application.getApplicationId();
        this.applicantName = application.getStudent().getUserName();
        this.applicantEmail = application.getStudent().getUserEmail();
        this.studentSkills = application.getStudent().getStudentSkills();
        this.studentId=application.getStudent().getUserId();
    }

    private String internshipTitle;
    private StudentApplicationStatus status;
    private String contactPerson;
    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    private Long studentId;
    /**
     * The Student skills.
     */
    Set<String> studentSkills;

}
