package com.subodh.InternshipPortal.services;


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
    public void sendMail(String toEmail, String subject, String body);
}
