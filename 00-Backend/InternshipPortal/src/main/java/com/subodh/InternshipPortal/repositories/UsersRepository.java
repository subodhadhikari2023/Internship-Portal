package com.subodh.InternshipPortal.repositories;



import com.subodh.InternshipPortal.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * The interface Users repository.
 */
public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * Find by user email users.
     *
     * @param userEmail the user email
     * @return the users
     */
    Users findByUserEmail(String userEmail);


}
