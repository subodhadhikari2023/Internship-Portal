package com.subodh.InternshipPortal.modals;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Data
public class Certificate {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(length = 36, nullable = false, updatable = false)
    private String certificateId;

    private String certificateName;

    private LocalDate issueDate;

    @OneToOne
    @JoinColumn(name = "internship_students_id", referencedColumnName = "studentInternshipId")
    private InternshipStudents internshipStudents;

    @OneToOne
    private Users student;

    private String certificateFilePath;

}
