package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.entities.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByInternship(Internship internship);
}
