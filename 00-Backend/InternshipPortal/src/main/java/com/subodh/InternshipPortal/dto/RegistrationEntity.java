package com.subodh.InternshipPortal.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private Number userPhoneNumber;


}
