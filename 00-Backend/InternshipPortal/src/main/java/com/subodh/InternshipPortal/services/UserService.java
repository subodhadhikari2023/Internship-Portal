package com.subodh.InternshipPortal.services;
import com.subodh.InternshipPortal.entities.Users;
import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {


    /**
     * Save user users.
     *
     * @param user the user
     * @return the users
     */
    Users saveUser(Users user);

    /**
     * Verify user credentials string.
     *
     * @param user the user
     * @return the string
     */
    String verifyUserCredentials(Users user);

    /**
     * Find all users list.
     *
     * @return the list
     */
    List<Users> findAllUsers();

    /**
     * Verify otp boolean.
     *
     * @param user the user
     * @param otp  the otp
     * @return the boolean
     */
    boolean verifyOtp(Users user, Long otp);

    /**
     * Email exists boolean.
     *
     * @param userEmail the user email
     * @return the boolean
     */
    boolean emailExists(String userEmail);
}
