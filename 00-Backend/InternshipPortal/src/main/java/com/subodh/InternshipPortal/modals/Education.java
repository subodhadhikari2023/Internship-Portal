package com.subodh.InternshipPortal.modals;

import jakarta.persistence.*;
import lombok.Data;

/**
 * The type Education.
 */
@Data
@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "education")
    private Users users;

    @OneToOne(cascade = CascadeType.ALL)
    private SecondaryEducation secondaryEducation;


    @OneToOne(cascade = CascadeType.ALL)
    private HigherSecondaryEducation higherSecondaryEducation;

    @OneToOne(cascade = CascadeType.ALL)
    private DiplomaEducation diplomaEducation;

    @OneToOne(cascade = CascadeType.ALL)
    private BachelorsEducation bachelorsEducation;

    @OneToOne(cascade = CascadeType.ALL)
    private MastersEducation mastersEducation;


}
