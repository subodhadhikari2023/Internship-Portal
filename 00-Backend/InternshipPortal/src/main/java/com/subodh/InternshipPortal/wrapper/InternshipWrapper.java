package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * The type Internship wrapper.
 */
@Data
@AllArgsConstructor
public class InternshipWrapper {
    private Long internshipId;
    private String internshipName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contactPerson;
    private InternshipStatus status;
    private Set<String> requiredSkills;
    private String educationalQualification;
    private WorkMode workMode;
    private String department;


    /**
     * Instantiates a new Internship wrapper.
     *
     * @param internship the internship
     */
    public InternshipWrapper(Internship internship) {
        this.internshipName = internship.getInternshipName();
        this.description = internship.getDescription();
        this.startDate = internship.getStartDate();
        this.endDate = internship.getEndDate();
        this.contactPerson = internship.getCreatedBy().getUserEmail();
        this.status = internship.getStatus();
        this.internshipId = internship.getInternshipId();
        this.requiredSkills = internship.getRequiredSkills();
        this.educationalQualification = internship.getEducationalQualifications();
        this.workMode = internship.getWorkMode();
        this.department = internship.getDepartment().getDepartmentName();

    }


}
