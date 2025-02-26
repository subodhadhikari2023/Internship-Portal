package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByInternshipAndStudent(Internship internship, Users student);

}
