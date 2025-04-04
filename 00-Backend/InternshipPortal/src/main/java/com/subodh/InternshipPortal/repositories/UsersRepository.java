package com.subodh.InternshipPortal.repositories;



import com.subodh.InternshipPortal.modals.Roles;
import com.subodh.InternshipPortal.modals.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;
import java.util.List;


/**
 * The interface Users repository.
 */
@CrossOrigin
public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * Find by user email users.
     *
     * @param userEmail the user email
     * @return the users
     */
    Users findByUserEmail(String userEmail);


    /**
     * Find by user id users.
     *
     * @param userId the user id
     * @return the users
     */
    Users findByUserId(Long userId);

    List<Users> findAllByRolesIn(Collection<List<Roles>> roles);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.roleName = :roleName")
    List<Users> findAllByRoleName(@Param("roleName") String roleName);

}
