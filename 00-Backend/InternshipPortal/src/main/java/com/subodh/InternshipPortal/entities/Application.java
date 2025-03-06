package com.subodh.InternshipPortal.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;


/**
 * The type Application.
 */
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
    @JsonBackReference
    @JoinColumn(name = "internship_id", referencedColumnName = "internship_id")
    private Internship internship;


    /**
     * The Status.
     */
    @Enumerated(EnumType.STRING)
    StudentApplicationStatus status;


}
