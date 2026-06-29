package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.repositories.RegistrationRepository;
import com.subodh.InternshipPortal.services.RegistrationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Registration service.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;

    /**
     * Instantiates a new Registration service.
     *
     * @param registrationRepository the registration repository
     */
    public RegistrationServiceImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    /**
     * {@inheritDoc}
     * Persists the registration entity to the temporary registration table.
     */
    @Override
    public RegistrationEntity save(RegistrationEntity registrationEntity) {
        return registrationRepository.save(registrationEntity);
    }

    /**
     * {@inheritDoc}
     * Looks up a pending registration by the user's email address.
     */
    @Override
    public RegistrationEntity findByEmail(String email) {
        return registrationRepository.findByUserEmail(email);
    }

    /**
     * {@inheritDoc}
     * Returns all pending registrations that share the given email address.
     */
    @Override
    public List<RegistrationEntity> findAllByEmail(String email) {
        return registrationRepository.findAllByUserEmail(email);
    }

    /**
     * {@inheritDoc}
     * Removes the pending registration row for the given user, typically called after OTP verification.
     */
    @Override
    public void delete(RegistrationEntity tempUser) {
        RegistrationEntity byUserEmail = registrationRepository.findByUserEmail(tempUser.getUserEmail());
        registrationRepository.deleteByUserEmail(byUserEmail.getUserEmail());
    }
}
