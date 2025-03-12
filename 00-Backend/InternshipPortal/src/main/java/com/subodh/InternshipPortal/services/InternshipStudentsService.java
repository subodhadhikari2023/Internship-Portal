package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface InternshipStudentsService {
    List<InternshipStudentsWrapper> findAllByStudentId(Long userId);

    List<InternshipStudentsWrapper> findAllStudentsOfInternshipsCreated(String userEmail);
}
