package com.subodh.InternshipPortal.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.enums.WorkMode;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * The type Internship.
 */
@Entity
@Table
@Data
public class Internship {
    /**
     * The Internship id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internship_id")
    private Long internshipId;
    /**
     * The Internship name.
     */
    String internshipName;
    /**
     * The Start date.
     */
    LocalDate startDate;
    /**
     * The End date.
     */
    LocalDate endDate;

    private String educationalQualifications;


    @Lob
    private String description;

    /**
     * The Created by.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructors_id", referencedColumnName = "user_id")
    Users createdBy;

    @Enumerated(EnumType.STRING)
    private InternshipStatus status;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @ElementCollection
    @CollectionTable(name = "internship_skills", joinColumns = @JoinColumn(name = "internship_id"))
    @Column(name = "skills")
    private Set<String> requiredSkills;

    @OneToMany(mappedBy = "internship")
    @JsonManagedReference
    List<Application> applications;

}
