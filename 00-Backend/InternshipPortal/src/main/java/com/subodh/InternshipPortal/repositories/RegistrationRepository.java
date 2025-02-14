package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {
    RegistrationEntity findByUserEmail(String userEmail);

    List<RegistrationEntity> findAllByUserEmail(String userEmail);

    void deleteByUserEmail(String userEmail);
}
