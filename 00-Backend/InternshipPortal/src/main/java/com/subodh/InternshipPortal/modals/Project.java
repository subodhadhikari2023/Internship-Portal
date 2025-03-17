package com.subodh.InternshipPortal.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
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


    private String fileName;

    private String fileType;


    private String filePath;

    @Column(nullable = false)
    private LocalDate submissionDate;

    private LocalDate uploadDate;

    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;




}
