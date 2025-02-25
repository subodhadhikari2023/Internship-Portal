package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.entities.Internship;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InternshipWrapper {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    String contactPerson;

    public InternshipWrapper(Internship internship) {
        this.title = internship.getInternshipName();
        this.description = internship.getDescription();
        this.startDate = internship.getStartDate();
        this.endDate = internship.getEndDate();
        this.contactPerson = internship.getCreatedBy().getUserEmail();
    }
}
