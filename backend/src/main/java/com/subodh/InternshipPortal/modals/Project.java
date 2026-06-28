package com.subodh.InternshipPortal.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Mutability;

import java.time.LocalDate;

/**
 * The type Project.
 */
@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String projectDescription;

    private String projectDescriptionFilePath;

    private String projectFile;

    private String projectFileType;
///storage/Internship-Portal/Information Technology/Data Science Internship/subodhadhikari929@gmail.com/Data Science Internship/project-files/projectData.pdf
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
