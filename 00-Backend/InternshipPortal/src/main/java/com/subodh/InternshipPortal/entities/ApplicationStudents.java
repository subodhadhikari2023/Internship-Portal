package com.subodh.InternshipPortal.entities;

import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class ApplicationStudents {


    @Id
    private Long applicationStudentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users student;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;


    @Enumerated(EnumType.STRING)
    private StudentInternshipStatus status;

}
