package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.exceptions.InternshipCreationFailedException;
import com.subodh.InternshipPortal.repositories.InternshipRepository;
import com.subodh.InternshipPortal.services.InternshipService;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternshipServiceImpl implements InternshipService {
    private final InternshipRepository internshipRepository;

    public InternshipServiceImpl(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    @Override
    public Internship saveInternship(Internship internship) {
        try {
            return internshipRepository.save(internship);

        } catch (DataIntegrityViolationException e) {
            throw new InternshipCreationFailedException("Internship creation failed due to duplicate or invalid data.");
        } catch (IllegalArgumentException e) {
            throw new InternshipCreationFailedException("Invalid internship data provided.");
        } catch (Exception e) {
            throw new InternshipCreationFailedException("Unexpected error while creating internship: " + e.getMessage());
        }
    }

    @Override
    public boolean findByInternshipId(Long internshipId) {
        return internshipRepository.findById(internshipId).isPresent();
    }

    @Override
    public List<InternshipWrapper> findAll() {
        List<Internship> internshipList = internshipRepository.findAll();

            return internshipList.isEmpty()?null: internshipRepository.findAll().stream()
                    .map(InternshipWrapper::new)
                    .toList();

    }
}

