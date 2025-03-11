package com.subodh.InternshipPortal.services.Implementation;


import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.UsersRepository;

import com.subodh.InternshipPortal.services.RegistrationService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RegistrationService registrationService;
    private final UsersRepository usersRepository;


    /**
     * Instantiates a new User service implementation.
     *
     * @param userRepository      the user repository
     * @param auth                the auth
     * @param passwordEncoder     the password encoder
     * @param jwtUtils            the jwt utils
     * @param registrationService the registration service
     */
    @Autowired
    public UserServiceImplementation(UsersRepository userRepository, AuthenticationManager auth, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RegistrationService registrationService, UsersRepository usersRepository) {
        this.userRepository = userRepository;
        this.auth = auth;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.registrationService = registrationService;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public Users saveUser(RegistrationEntity tempUser) {
        Users user = new Users();
        user.setUserEmail(tempUser.getUserEmail());
        user.setUserPassword(tempUser.getUserPassword());
        user.setUserName(tempUser.getUserName());
        user.setUserPhoneNumber(tempUser.getUserPhoneNumber());
        user.addRole("ROLE_STUDENT");
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        registrationService.delete(tempUser);
        return userRepository.save(user);
    }

    @Override

    public String verifyUserCredentials(Users user) throws Exception {
        Authentication authenticate = auth.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword()));
        boolean authenticated = authenticate.isAuthenticated();
        if (authenticated)
            return jwtUtils.generateToken(authenticate);
        throw new RuntimeException("Invalid user credentials");
    }

    @Override
    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean verifyOtp(Users user, Long otp) {
        return true;
    }

    @Override
    public boolean emailExists(String userEmail) {
        return userRepository.findByUserEmail(userEmail)!= null;
    }

    @Override
    public Users findByUserEmail(String username) {
        return usersRepository.findByUserEmail(username);
    }

}
