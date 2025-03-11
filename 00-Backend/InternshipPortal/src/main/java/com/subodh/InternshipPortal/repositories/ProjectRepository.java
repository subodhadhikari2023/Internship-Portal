package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
