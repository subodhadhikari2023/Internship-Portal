package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Otp repository.
 */
public interface OTPRepository extends JpaRepository<OneTimePassword,Long> {
    /**
     * Find by user email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<OneTimePassword> findByUserEmail(String email);

    /**
     * Delete all by user email.
     *
     * @param userEmail the user email
     */
    void deleteAllByUserEmail(String userEmail);
//    Optional<OneTimePassword>
}
