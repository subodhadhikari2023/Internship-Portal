package com.subodh.InternshipPortal.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.time.Year;

@Entity
@Data
public class MastersEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long masterEducationId;

    private String collegeName;

    private String courseName;

    private Year startYear;

    private Year passingYear;

    @DecimalMin(value = "0.00", inclusive = false)
    @DecimalMax(value = "100.00", inclusive = false)
    private Long passingPercentage;
}
