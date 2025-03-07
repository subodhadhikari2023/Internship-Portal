package com.subodh.InternshipPortal.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "education")
    private Users users;

    @OneToOne
    private SecondaryEducation secondaryEducation;


    @OneToOne
    private HigherSecondaryEducation higherSecondaryEducation;

    @OneToOne
    private DiplomaEducation diplomaEducation;

    @OneToOne
    private BachelorsEducation bachelorsEducation;

    @OneToOne
    private MastersEducation mastersEducation;


}
