package com.subodh.InternshipPortal.modals;

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
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private Users student;

    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;

    @OneToMany(mappedBy = "internshipStudents", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Project> projects;



}
