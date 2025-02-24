package com.subodh.InternshipPortal.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
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

    /**
     * The Created by.
     */
    @ManyToOne
    @JoinColumn(name = "instructors_id")
    Users createdBy;

    @ManyToMany
    @JoinTable(
            name = "internship_students",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Users> students;


}
