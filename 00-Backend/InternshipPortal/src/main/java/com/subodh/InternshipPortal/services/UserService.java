package com.subodh.InternshipPortal.services;
import com.subodh.InternshipPortal.wrapper.InstructorWrapper;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.wrapper.StudentWrapper;

import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    Users saveUser(RegistrationEntity user);

    /**
     * Verify user credentials string.
     *
     * @param user the user
     * @return the string
     * @throws Exception the exception
     */
    String verifyUserCredentials(Users user) throws Exception;

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

    Users findByUserEmail(String username);

    StudentWrapper updateStudent(UserDetails userDetails, StudentWrapper userWrapper);

    StudentWrapper updateProfilePicture(UserDetails userDetails, MultipartFile file);

    StudentWrapper uploadResume(UserDetails userDetails, MultipartFile file);

    Resource downloadResume(String filePath) throws IOException;

    StudentWrapper findStudentByStudentId(Long studentId);

    InstructorWrapper getProfileDetails(UserDetails userDetails);

    InstructorWrapper updateProfilePictureOfInstructors(UserDetails userDetails, MultipartFile file);

}
