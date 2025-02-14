package com.subodh.InternshipPortal.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * The type One time password.
 */
@Data
@Entity
public class OneTimePassword {

   private String oneTimePassword;
    private String userEmail;
    private LocalDateTime expiryTime;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
