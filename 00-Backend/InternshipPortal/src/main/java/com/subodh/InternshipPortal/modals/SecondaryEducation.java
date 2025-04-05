package com.subodh.InternshipPortal.modals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;


import java.time.Year;

/**
 * The type Secondary education.
 */
@Data
@Entity
public class SecondaryEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;

    private String boardName;

    private Year passingYear;

    @DecimalMin(value = "0.00",inclusive = false)
    @DecimalMax(value = "100.00",inclusive = false)
    private Double passingPercentage;




}
