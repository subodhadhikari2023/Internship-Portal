package com.subodh.InternshipPortal.services.Implementation;


import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.repositories.UsersRepository;

import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type User service implementation.
 */
@Service
public class UserServiceImplementation implements UserService {

    private final UsersRepository userRepository;
    private final AuthenticationManager auth;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Autowired
    public UserServiceImplementation(UsersRepository userRepository, AuthenticationManager auth, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.auth = auth;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Users saveUser(Users user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }
    @Override
    public String verifyUserCredentials(Users user) {
        Authentication authenticate = auth.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword()));
        boolean authenticated = authenticate.isAuthenticated();
        if (authenticated)
           return jwtUtils.generateToken(authenticate);
        return "Not Authenticated";
    }

    @Override
    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean verifyOtp(Users user, Long otp) {
        return true;
    }

}
