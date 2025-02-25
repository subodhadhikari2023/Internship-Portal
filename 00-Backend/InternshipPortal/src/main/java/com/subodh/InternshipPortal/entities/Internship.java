package com.subodh.InternshipPortal.entities;

import com.subodh.InternshipPortal.enums.InternshipStatus;
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
    Long internshipId;
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
    @Lob
    private String description;

    /**
     * The Created by.
     */
    @ManyToOne
    @JoinColumn(name = "instructors_id",referencedColumnName = "user_id")
    Users createdBy;

    @Enumerated(EnumType.STRING)
    private InternshipStatus status;

    @ElementCollection
    @CollectionTable(name = "application_skills",joinColumns = @JoinColumn(name = "application_id"))
    private List<String> requiredSkills;


}
