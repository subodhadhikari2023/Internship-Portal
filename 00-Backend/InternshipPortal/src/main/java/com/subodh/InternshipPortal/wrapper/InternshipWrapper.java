package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.enums.InternshipStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class InternshipWrapper {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    String contactPerson;
    InternshipStatus status;

    public InternshipWrapper(Internship internship) {
        this.title = internship.getInternshipName();
        this.description = internship.getDescription();
        this.startDate = internship.getStartDate();
        this.endDate = internship.getEndDate();
        this.contactPerson = internship.getCreatedBy().getUserEmail();
        this.status = internship.getStatus();
        this.id = internship.getInternshipId();
    }


}
