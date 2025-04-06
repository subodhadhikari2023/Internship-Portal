package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.InternshipStudentsService;
import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The type Internship students service.
 */
@Slf4j
@Service
public class InternshipStudentsServiceImpl implements InternshipStudentsService {
    private final InternshipStudentRepository internshipStudentRepository;
    private final UsersRepository usersRepository;

    /**
     * Instantiates a new Internship students service.
     *
     * @param internshipStudentRepository the internship student repository
     * @param usersRepository             the users repository
     */
    public InternshipStudentsServiceImpl(InternshipStudentRepository internshipStudentRepository, UsersRepository usersRepository) {
        this.internshipStudentRepository = internshipStudentRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public List<InternshipStudentsWrapper> findAllByStudentId(Long userId) {
        List<InternshipStudents> internships = internshipStudentRepository.findAllByStudent(usersRepository.findByUserId(userId));
        return internships.isEmpty() ? Collections.emptyList() : internships.stream().map(InternshipStudentsWrapper::new).collect(Collectors.toList());
    }


    @Override
    public List<InternshipStudentsWrapper> findAllStudentsOfInternshipsCreated(String userEmail) {
        Users user = usersRepository.findByUserEmail(userEmail);
        List<InternshipStudents> internshipStudents = internshipStudentRepository.findAllByInternship_CreatedBy(user);
        return internshipStudents.isEmpty() ? Collections.emptyList() : internshipStudents.stream().map(InternshipStudentsWrapper::new).collect(Collectors.toList());
    }



}
