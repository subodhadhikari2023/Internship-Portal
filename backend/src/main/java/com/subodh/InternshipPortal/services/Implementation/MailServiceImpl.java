package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.services.MailService;
import jakarta.validation.constraints.Digits;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
    public void sendApplicationStatusMail(String toEmail, String studentName, String internshipTitle, StudentApplicationStatus status, String sender, String department) {
        String subject = "Update on Your Internship Application";
        String body = prepareMailBody(studentName, internshipTitle, status, sender, department);
        sendMail(toEmail, subject, body);
    }

    @Override
    public void sendPasswordResetMail(String userEmail, String oneTimePassword) {
        String subject = "Password Reset";
        String body = preparePasswordResetMail(userEmail, oneTimePassword);
        sendMail(userEmail, subject, body);
    }

    @Override
    public void sendProjectAllocationMail(String userName, String projectName, LocalDate submissionDate) {
        String subject = "Project Allocation";
        String body = prepareProjectAllocationMail(userName, projectName, submissionDate);
        sendMail(userName, subject, body);
    }

    @Override
    public void sendProjectStatusChangeMail(String userEmail, String projectName, StudentInternshipStatus status, LocalDate now) {
        String subject = "Project Status Change Notification";
        String body = prepareProjectStatusChangeMail(userEmail, projectName, status, now);
        sendMail(userEmail, subject, body);
    }

    @Override
    public void sendInstructorCreationMail(Users user) {
        String subject = "Instructor Creation Notification";
        String body = prepareInstructorCreationMail(user.getUserName(),user.getUserEmail(),user.getUserPhoneNumber(),user.getDepartment().getDepartmentName(),user.getUserEmail()+"@123");
        sendMail(user.getUserEmail(), subject, body);
    }

    private String prepareInstructorCreationMail(
            String userName,
            String userEmail,
            @Digits(integer = 15, fraction = 0, message = "Phone number must be a valid number with at most 15 digits")
            Long userPhoneNumber,
            String departmentName,
            String temporaryPassword
    ) {
        return String.format(
                """
                Dear %s,
       \s
                Welcome aboard! 🎉 You have been successfully registered as an instructor in the %s department.
       \s
                Here are your login credentials:
       \s
                ▸ Email: %s \s
                ▸ Temporary Password: %s \s
                ▸ Phone: %s
       \s
                Please make sure to change your password after your first login.
       \s
                If you face any issues accessing your account, feel free to reach out to the admin team.
       \s
                We’re excited to have you join the platform!
       \s
                Best regards, \s
                Internship Portal Admin Team
               \s""",
                userName,
                departmentName,
                userEmail,
                temporaryPassword,
                userPhoneNumber
        );
    }

    private String prepareProjectStatusChangeMail(String userEmail, String projectName, StudentInternshipStatus status, LocalDate now) {
        return String.format(
                """
                                 Dear %s,
                        \s
                                 This is to inform you that the status of your project titled "%s" has been updated to **%s** as of %s.
                        \s
                                 Please log in to the portal to view further details or next steps.
                        \s
                                 Regards, \s
                                 Government of Sikkim
                                \s""",
                userEmail, projectName, status.name(), now
        );
    }


    private String prepareProjectAllocationMail(String userName, String projectName, LocalDate submissionDate) {
        return String.format(
                """
                        Dear %s,\s
                        \s
                        We are pleased to inform you that you have been allocated the project titled "%s".
                        Please ensure that the project is submitted by %s.
                        
                        Wishing you all the best for a successful completion.
                        
                        Regards,
                        Government of Sikkim""",
                userName, projectName, submissionDate
        );
    }


    private String preparePasswordResetMail(String userEmail, String oneTimePassword) {
        return String.format(
                "Dear %s,\n\nWe hope this email finds you well.\n\nThe following is the One Time Password to change the password for the account associated with %s! \n\n %s \n\n Regards, \nGovernment of Sikkim\n",
                userEmail, userEmail, oneTimePassword
        );
    }

    private String prepareMailBody(String studentName, String internshipTitle, StudentApplicationStatus status, String sender, String department) {
        String message = switch (status) {
            case ACCEPTED ->
                    "Congratulations! You have been selected for the internship. Further details will be shared soon.";
            case REJECTED ->
                    "Unfortunately, we cannot move forward with your application at this time. We encourage you to apply for future opportunities.";
            default -> "Your application status has been updated. Please check your account for more details.";
        };

        return String.format(
                "Dear %s,\n\nWe hope this email finds you well. We would like to inform you about the status of your application for the internship: \n%s.\n\nStatus: %s\n\n%s\n\nBest regards,\n%s\nGovernment of Sikkim\n Department of %s",
                studentName, internshipTitle, status, message, sender, department
        );
    }
}

