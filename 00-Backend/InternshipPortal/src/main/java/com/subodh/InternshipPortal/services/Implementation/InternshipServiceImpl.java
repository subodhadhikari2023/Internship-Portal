package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.exceptions.InternshipCreationFailedException;
import com.subodh.InternshipPortal.repositories.InternshipRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.InternshipService;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InternshipServiceImpl implements InternshipService {
    private final InternshipRepository internshipRepository;
    private final UsersRepository usersRepository;

    public InternshipServiceImpl(InternshipRepository internshipRepository, UsersRepository usersRepository) {
        this.internshipRepository = internshipRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public InternshipWrapper saveInternship(Internship internship, String username) {
        try {
            Users user = usersRepository.findByUserEmail(username);
            internship.setCreatedBy(user);
            Set<String> requiredSkills = internship.getRequiredSkills().stream().map(String::trim).filter(skill -> !skill.isEmpty()).collect(Collectors.toSet());
            internship.setRequiredSkills(requiredSkills);
            Internship saved = internshipRepository.save(internship);
            return new InternshipWrapper(saved);

        } catch (DataIntegrityViolationException e) {
            throw new InternshipCreationFailedException("Internship creation failed due to duplicate or invalid data.");
        } catch (IllegalArgumentException e) {
            throw new InternshipCreationFailedException("Invalid internship data provided.");
        } catch (Exception e) {
            throw new InternshipCreationFailedException("Unexpected error while creating internship: " + e.getMessage());
        }
    }


    @Override
    public List<InternshipWrapper> findAll() {
        List<Internship> internshipList = internshipRepository.findAll();
        return internshipList.isEmpty() ? Collections.emptyList() : internshipRepository.findAll().stream()
                .map(InternshipWrapper::new)
                .toList();

    }

    @Override
    public List<InternshipWrapper> findAllByInstructor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails users) {
            String email = users.getUsername();
            Users instructor = usersRepository.findByUserEmail(email);

            List<Internship> internshipList = internshipRepository.findAllByCreatedBy(instructor);
            return internshipList.stream().map(InternshipWrapper::new).collect(Collectors.toList());
        }
        return Collections.emptyList();


    }
}

