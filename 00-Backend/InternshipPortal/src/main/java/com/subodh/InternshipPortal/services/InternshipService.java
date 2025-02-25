package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;

import java.util.List;

public interface InternshipService {

    Internship saveInternship(Internship internship);

    boolean findByInternshipId(Long internshipId);

    List<InternshipWrapper> findAll();
}
