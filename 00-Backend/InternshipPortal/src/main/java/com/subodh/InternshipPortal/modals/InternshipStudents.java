package com.subodh.InternshipPortal.modals;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import jakarta.persistence.*;
import lombok.Data;

/**
 * The type Internship students.
 */
@Entity
@Data
@Table(name = "internship_students")
public class InternshipStudents {
    @Id
    private Long studentInternshipId;

    @ManyToOne
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private Users student;

    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;

}
