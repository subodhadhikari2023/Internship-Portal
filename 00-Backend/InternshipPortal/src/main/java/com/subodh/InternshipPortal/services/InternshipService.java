package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Internship;

public interface InternshipService {

    Internship saveInternship(Internship internship);

    boolean findByInternshipId(Long internshipId);
}
