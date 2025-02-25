package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.RegistrationEntity;

import java.util.List;

public interface RegistrationService {
    RegistrationEntity save(RegistrationEntity registrationEntity);

    RegistrationEntity findByEmail(String email);
    List<RegistrationEntity> findAllByEmail(String email);

    void delete(RegistrationEntity tempUser);
}
