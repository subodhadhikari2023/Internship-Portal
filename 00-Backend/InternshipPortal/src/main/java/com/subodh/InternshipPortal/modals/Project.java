package com.subodh.InternshipPortal.modals;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private String projectDescription;

    @Column(nullable = false)
    private String fileName;

    private String fileType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDate uploadDate;

    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;

    @ManyToOne
    @JoinColumn(name = "student_internship_id", referencedColumnName = "studentInternshipId")
    private InternshipStudents internshipStudents;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;




}
