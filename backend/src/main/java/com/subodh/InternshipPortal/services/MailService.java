package com.subodh.InternshipPortal.services;


import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.Users;

import java.time.LocalDate;

/**
 * The interface Mail service.
 */
public interface MailService {
    /**
     * Send mail.
     *
     * @param toEmail the to email
     * @param subject the subject
     * @param body    the body
     */
    void sendMail(String toEmail, String subject, String body);

    /**
     * Send application status mail.
     *
     * @param toEmail         the to email
     * @param studentName     the student name
     * @param internshipTitle the internship title
     * @param status          the status
     * @param sender          the sender
     * @param department      the department
     */
    void sendApplicationStatusMail(String toEmail, String studentName, String internshipTitle, StudentApplicationStatus status,String sender, String department);


    /**
     * Send password reset mail.
     *
     * @param userEmail       the user email
     * @param oneTimePassword the one time password
     */
    void sendPasswordResetMail(String userEmail, String oneTimePassword);

    void sendProjectAllocationMail(String userName, String projectName, LocalDate submissionDate);

    void sendProjectStatusChangeMail(String userEmail, String projectName, StudentInternshipStatus status, LocalDate now);

    void sendInstructorCreationMail(Users user);
}
