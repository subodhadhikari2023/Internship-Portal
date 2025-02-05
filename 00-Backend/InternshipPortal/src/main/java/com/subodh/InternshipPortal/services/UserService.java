package com.subodh.InternshipPortal.services;


import com.subodh.InternshipPortal.entities.Users;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {


    Users saveUser(Users user);

    String verifyUserCredentials(Users user);

    List<Users> findAllUsers();
}
