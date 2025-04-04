package com.subodh.InternshipPortal.services;


import com.subodh.InternshipPortal.enums.StudentApplicationStatus;

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
    void sendApplicationStatusMail(String toEmail, String studentName, String internshipTitle, StudentApplicationStatus status,String sender, String department);


    void sendPasswordResetMail(String userEmail, String oneTimePassword);
}
