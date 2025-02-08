package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.services.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    final JavaMailSender mailSender;

    @Value("${mail.host.name}")
    private String mailHost;

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
}
