package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.RegistrationEntity;

import java.util.List;

/**
 * The interface Registration service.
 */
public interface RegistrationService {
    /**
     * Save registration entity.
     *
     * @param registrationEntity the registration entity
     * @return the registration entity
     */
    RegistrationEntity save(RegistrationEntity registrationEntity);

    /**
     * Find by email registration entity.
     *
     * @param email the email
     * @return the registration entity
     */
    RegistrationEntity findByEmail(String email);

    /**
     * Find all by email list.
     *
     * @param email the email
     * @return the list
     */
    List<RegistrationEntity> findAllByEmail(String email);

    /**
     * Delete.
     *
     * @param tempUser the temp user
     */
    void delete(RegistrationEntity tempUser);
}
