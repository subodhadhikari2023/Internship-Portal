package com.subodh.InternshipPortal.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The type Application.
 */
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentApplication {


    @Id
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


    public StudentApplication(Application application) {
        this.applicationId = application.getApplicationId();
        this.setInternship(application.getInternship());
        this.setStudent(application.getStudent());
        this.setStatus(application.getStatus());
    }
}
