package com.subodh.InternshipPortal.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

/**
 * The type Internship students.
 */
@Entity
@Data
@Table(name = "internship_students")
public class InternshipStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentInternshipId;

    @ManyToOne
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private Users student;

    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;


    @OneToMany
    @JoinTable(
            name = "internship_students_projects",
            joinColumns = @JoinColumn(name = "student_internship_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;


}
