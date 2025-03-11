package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.services.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * The type Mail service.
 */
@Service
@Async("taskExecutor")
public class MailServiceImpl implements MailService {
    /**
     * The Mail sender.
     */
    final JavaMailSender mailSender;

    @Value("${mail.host.name}")
    private String mailHost;

    /**
     * Instantiates a new Mail service.
     *
     * @param mailSender the mail sender
     */
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }


    @Override
    public void sendMail(String toEmail, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailHost);
        mailMessage.setTo(toEmail);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);
        mailSender.send(mailMessage);

    }

    @Override
    public void sendApplicationStatusMail(String toEmail, String studentName, String internshipTitle, StudentApplicationStatus status,String sender, String department) {
        String subject = "Update on Your Internship Application";
        String body = prepareMailBody(studentName, internshipTitle, status,sender,department);
        sendMail(toEmail, subject, body);
    }

    private String prepareMailBody(String studentName, String internshipTitle, StudentApplicationStatus status,String sender, String department) {
        String message = switch (status) {
            case ACCEPTED ->
                    "Congratulations! You have been selected for the internship. Further details will be shared soon.";
            case REJECTED ->
                    "Unfortunately, we cannot move forward with your application at this time. We encourage you to apply for future opportunities.";
            default -> "Your application status has been updated. Please check your account for more details.";
        };

        return String.format(
                "Dear %s,\n\nWe hope this email finds you well. We would like to inform you about the status of your application for the internship: \n%s.\n\nStatus: %s\n\n%s\n\nBest regards,\n%s\nGovernment of Sikkim\n Department of %s",
                studentName, internshipTitle, status, message,sender,department
        );
    }
}

