package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The type Application wrapper.
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
        this.applicationId=application.getApplicationId();
    }

    private String internshipTitle;
    private StudentApplicationStatus status;
    private String contactPerson;
    private Long applicationId;

}
