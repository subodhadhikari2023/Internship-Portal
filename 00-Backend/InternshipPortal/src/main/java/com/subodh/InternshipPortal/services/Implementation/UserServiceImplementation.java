package com.subodh.InternshipPortal.services.Implementation;


import com.subodh.InternshipPortal.modals.*;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.repositories.UsersRepository;

import com.subodh.InternshipPortal.services.RegistrationService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.wrapper.StudentWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
    public UserServiceImplementation(UsersRepository userRepository, AuthenticationManager auth, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RegistrationService registrationService) {
        this.userRepository = userRepository;
        this.auth = auth;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.registrationService = registrationService;
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
        return userRepository.findByUserEmail(userEmail) != null;
    }

    @Override
    public Users findByUserEmail(String username) {
        return userRepository.findByUserEmail(username);
    }

    @Override
    @Transactional
    public StudentWrapper updateStudent(UserDetails userDetails, StudentWrapper userWrapper) {
        Users user = userRepository.findByUserEmail(userDetails.getUsername());

        // Ensure education object exists
        user.setEducation(Optional.ofNullable(user.getEducation()).orElse(new Education()));

        // Update user details
        updateIfNotNull(userWrapper.getUserName(), user::setUserName);
        updateIfNotNull(userWrapper.getPhoneNumber(), user::setUserPhoneNumber);
        updateIfNotNull(userWrapper.getSkills(), user::setStudentSkills);

        // Update education details
        updateEducationDetails(user, userWrapper);

        return new StudentWrapper(userRepository.save(user));
    }

    private void updateEducationDetails(Users user, StudentWrapper userWrapper) {
        // Initialize education objects if null
        Education education = user.getEducation();
        education.setSecondaryEducation(Optional.ofNullable(education.getSecondaryEducation()).orElse(new SecondaryEducation()));
        education.setHigherSecondaryEducation(Optional.ofNullable(education.getHigherSecondaryEducation()).orElse(new HigherSecondaryEducation()));
        education.setDiplomaEducation(Optional.ofNullable(education.getDiplomaEducation()).orElse(new DiplomaEducation()));
        education.setBachelorsEducation(Optional.ofNullable(education.getBachelorsEducation()).orElse(new BachelorsEducation()));
        education.setMastersEducation(Optional.ofNullable(education.getMastersEducation()).orElse(new MastersEducation()));

        // Update fields using helper function
        SecondaryEducation secEdu = education.getSecondaryEducation();
        updateIfNotNull(userWrapper.getSecondarySchoolName(), secEdu::setSchoolName);
        updateIfNotNull(userWrapper.getSecondaryPassingPercentage(), secEdu::setPassingPercentage);
        updateIfNotNull(userWrapper.getSecondaryBoardName(), secEdu::setBoardName);
        updateIfNotNull(userWrapper.getSecondaryPassingYear(), secEdu::setPassingYear);

        HigherSecondaryEducation highSecEdu = education.getHigherSecondaryEducation();
        updateIfNotNull(userWrapper.getHigherSecondarySchoolName(), highSecEdu::setSchoolName);
        updateIfNotNull(userWrapper.getHigherSecondaryPassingPercentage(), highSecEdu::setPassingPercentage);
        updateIfNotNull(userWrapper.getHigherSecondaryBoardName(), highSecEdu::setBoardName);
        updateIfNotNull(userWrapper.getHigherSecondaryStream(), highSecEdu::setStream);
        updateIfNotNull(userWrapper.getHigherSecondaryPassingYear(), highSecEdu::setPassingYear);

        DiplomaEducation dipEdu = education.getDiplomaEducation();
        updateIfNotNull(userWrapper.getDiplomaCollegeName(), dipEdu::setCollegeName);
        updateIfNotNull(userWrapper.getDiplomaCourseName(), dipEdu::setCourseName);
        updateIfNotNull(userWrapper.getDiplomaStartYear(), dipEdu::setStartYear);
        updateIfNotNull(userWrapper.getDiplomaPassingYear(), dipEdu::setPassingYear);
        updateIfNotNull(userWrapper.getDiplomaPassingPercentage(), dipEdu::setPassingPercentage);

        BachelorsEducation bachEdu = education.getBachelorsEducation();
        updateIfNotNull(userWrapper.getBachelorsCollegeName(), bachEdu::setCollegeName);
        updateIfNotNull(userWrapper.getBachelorsCourseName(), bachEdu::setCourseName);
        updateIfNotNull(userWrapper.getBachelorsStartYear(), bachEdu::setStartYear);
        updateIfNotNull(userWrapper.getBachelorsPassingYear(), bachEdu::setPassingYear);
        updateIfNotNull(userWrapper.getBachelorsPassingPercentage(), bachEdu::setPassingPercentage);

        MastersEducation mastEdu = education.getMastersEducation();
        updateIfNotNull(userWrapper.getMastersCollegeName(), mastEdu::setCollegeName);
        updateIfNotNull(userWrapper.getMastersCourseName(), mastEdu::setCourseName);
        updateIfNotNull(userWrapper.getMastersStartYear(), mastEdu::setStartYear);
        updateIfNotNull(userWrapper.getMastersPassingYear(), mastEdu::setPassingYear);
        updateIfNotNull(userWrapper.getMastersPassingPercentage(), mastEdu::setPassingPercentage);
    }

    // Helper method to update only if the value is not null
    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
