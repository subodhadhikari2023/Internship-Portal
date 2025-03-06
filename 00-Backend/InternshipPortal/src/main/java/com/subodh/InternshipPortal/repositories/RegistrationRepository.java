package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Registration repository.
 */
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {
    /**
     * Find by user email registration entity.
     *
     * @param userEmail the user email
     * @return the registration entity
     */
    RegistrationEntity findByUserEmail(String userEmail);

    /**
     * Find all by user email list.
     *
     * @param userEmail the user email
     * @return the list
     */
    List<RegistrationEntity> findAllByUserEmail(String userEmail);

    /**
     * Delete by user email.
     *
     * @param userEmail the user email
     */
    void deleteByUserEmail(String userEmail);
}
