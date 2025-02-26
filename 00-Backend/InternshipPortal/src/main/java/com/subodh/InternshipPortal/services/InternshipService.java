package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;

import java.util.List;
import java.util.Optional;

public interface InternshipService {

    InternshipWrapper saveInternship(Internship internship);

    List<InternshipWrapper> findAll();
    List<InternshipWrapper> findAllByInstructor();
}
