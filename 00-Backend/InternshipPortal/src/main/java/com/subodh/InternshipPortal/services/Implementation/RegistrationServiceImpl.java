package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.repositories.RegistrationRepository;
import com.subodh.InternshipPortal.services.RegistrationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
    public RegistrationEntity save(RegistrationEntity registrationEntity) {
        return registrationRepository.save(registrationEntity);
    }

    @Override
    public RegistrationEntity findByEmail(String email) {
        return registrationRepository.findByUserEmail(email);
    }

    @Override
    public List<RegistrationEntity> findAllByEmail(String email) {
        return registrationRepository.findAllByUserEmail(email);
    }

    @Override
    public void delete(RegistrationEntity tempUser) {
        RegistrationEntity byUserEmail = registrationRepository.findByUserEmail(tempUser.getUserEmail());
        registrationRepository.deleteByUserEmail(byUserEmail.getUserEmail());
    }
}
