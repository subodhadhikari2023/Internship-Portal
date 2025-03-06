package com.subodh.InternshipPortal.wrapper;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


/**
 * The type Registration entity.
 */
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
