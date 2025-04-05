package com.subodh.InternshipPortal.modals;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

/**
 * The type Certificate.
 */
@Entity
@Data
public class Certificate {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String certificateId;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String certificateFilePath;

    private LocalDate issueDate;

    @OneToOne
    @JoinColumn(name = "internship_students_id", referencedColumnName = "studentInternshipId")
    private InternshipStudents internshipStudents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private Users student;


}
