package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT a FROM Application a WHERE a.internship.internshipId IN :internshipId and a.student.userId IN :studentId")
    Optional<Application> existsByInternshipAndStudent(Long internshipId, Long studentId);


    @Query("SELECT a FROM Application a WHERE a.internship.internshipId IN :internshipIds")
    List<Application> findByInternshipIds(@Param("internshipIds") List<Long> internshipIds);
}
