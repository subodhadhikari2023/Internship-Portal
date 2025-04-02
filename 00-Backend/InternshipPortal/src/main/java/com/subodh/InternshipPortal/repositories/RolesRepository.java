package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    List<Roles> findByRoleName(String roleName);
}
