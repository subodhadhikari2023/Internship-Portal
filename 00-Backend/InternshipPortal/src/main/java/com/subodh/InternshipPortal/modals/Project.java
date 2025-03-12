package com.subodh.InternshipPortal.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne
    @JoinColumn(nullable = false, name = "student_id", referencedColumnName = "user_id")
    private Users user;

//    @ManyToOne
//    @JoinColumn(name = "internship_id", referencedColumnName = "internship_id")
//    private Internship internship;

    @Column(nullable = false)
    private LocalDate uploadDate;

    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;

//    @ManyToOne
//    @JoinColumn(name = "student_internship_id",referencedColumnName = "student_internship_id")
//    private InternshipStudents internshipStudents;

    @ManyToOne
    @JoinColumn(name = "internship_id",referencedColumnName = "internship_id")
    @JsonBackReference
    private Internship internships;


}
