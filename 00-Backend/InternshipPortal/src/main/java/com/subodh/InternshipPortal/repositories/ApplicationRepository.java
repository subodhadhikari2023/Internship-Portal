package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByInternshipAndStudent(Internship internship, Users student);


    @Query("SELECT a FROM Application a WHERE a.internship.internshipId IN :internshipIds")
    List<Application> findByInternshipIds(@Param("internshipIds") List<Long> internshipIds);
}
