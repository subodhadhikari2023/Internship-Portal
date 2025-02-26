package com.subodh.InternshipPortal.entities;

import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Application {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;


    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private Users student;


    @ManyToOne
    @JoinColumn(name = "internship_id", referencedColumnName = "internship_id")
    private Internship internship;


    @Enumerated(EnumType.STRING)
    StudentApplicationStatus status;


}
