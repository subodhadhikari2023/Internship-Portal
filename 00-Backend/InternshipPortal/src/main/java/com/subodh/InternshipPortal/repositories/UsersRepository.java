package com.subodh.InternshipPortal.repositories;



import com.subodh.InternshipPortal.modals.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;


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
}
