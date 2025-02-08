package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OneTimePassword,Long> {
    Optional<OneTimePassword> findByUserEmail(String email);

    void deleteAllByUserEmail(String userEmail);
//    Optional<OneTimePassword>
}
