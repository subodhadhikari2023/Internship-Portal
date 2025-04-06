package com.subodh.InternshipPortal.services;
import com.subodh.InternshipPortal.wrapper.*;
import com.subodh.InternshipPortal.modals.Users;

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
    List<?> findAllUsers();

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

    /**
     * Find by user email users.
     *
     * @param username the username
     * @return the users
     */
    Users findByUserEmail(String username);

    /**
     * Update student student wrapper.
     *
     * @param userDetails the user details
     * @param userWrapper the user wrapper
     * @return the student wrapper
     */
    StudentWrapper updateStudent(UserDetails userDetails, StudentWrapper userWrapper);

    /**
     * Update profile picture student wrapper.
     *
     * @param userDetails the user details
     * @param file        the file
     * @return the student wrapper
     */
    StudentWrapper updateProfilePicture(UserDetails userDetails, MultipartFile file);

    /**
     * Upload resume student wrapper.
     *
     * @param userDetails the user details
     * @param file        the file
     * @return the student wrapper
     */
    StudentWrapper uploadResume(UserDetails userDetails, MultipartFile file);

    /**
     * Download resume resource.
     *
     * @param filePath the file path
     * @return the resource
     * @throws IOException the io exception
     */
    Resource downloadResume(String filePath) throws IOException;

    /**
     * Find student by student id student wrapper.
     *
     * @param studentId the student id
     * @return the student wrapper
     */
    StudentWrapper findStudentByStudentId(Long studentId);

    /**
     * Gets profile details.
     *
     * @param userDetails the user details
     * @return the profile details
     */
    InstructorWrapper getProfileDetails(UserDetails userDetails);

    /**
     * Update profile picture of instructors instructor wrapper.
     *
     * @param userDetails the user details
     * @param file        the file
     * @return the instructor wrapper
     */
    InstructorWrapper updateProfilePictureOfInstructors(UserDetails userDetails, MultipartFile file);

    /**
     * Update instructor instructor wrapper.
     *
     * @param userDetails       the user details
     * @param instructorWrapper the instructor wrapper
     * @return the instructor wrapper
     */
    InstructorWrapper updateInstructor(UserDetails userDetails, InstructorWrapper instructorWrapper);

    /**
     * Add instructor instructor wrapper.
     *
     * @param instructor the instructor
     * @return the instructor wrapper
     */
    InstructorWrapper addInstructor(InstructorWrapper instructor);

    /**
     * Find all students list.
     *
     * @return the list
     */
    List<StudentWrapper> findAllStudents();

    /**
     * Find all instructors list.
     *
     * @return the list
     */
    List<InstructorWrapper> findAllInstructors();

    /**
     * Forget password user wrapper.
     *
     * @param email the email
     * @return the user wrapper
     */
    UserWrapper forgetPassword(String email);

    /**
     * Reset password user found by email user wrapper.
     *
     * @param email    the email
     * @param password the password
     * @return the user wrapper
     */
    UserWrapper resetPasswordUserFoundByEmail(String email, String password);

    AdminWrapper updateAdminProfilePicture(UserDetails userDetails, MultipartFile file);

    AdminWrapper updateAdmin(UserDetails userDetails, AdminWrapper adminWrapper);
}
