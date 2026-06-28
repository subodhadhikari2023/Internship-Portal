package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Project repository.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
