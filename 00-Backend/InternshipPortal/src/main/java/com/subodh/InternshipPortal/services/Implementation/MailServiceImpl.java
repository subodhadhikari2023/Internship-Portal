package com.subodh.InternshipPortal.services.Implementation;

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

    @Async("taskExecutor")
    @Override
    public void sendMail(String toEmail, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailHost);
        mailMessage.setTo(toEmail);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);
        mailSender.send(mailMessage);

    }
}
