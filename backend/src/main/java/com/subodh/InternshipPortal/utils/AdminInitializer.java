package com.subodh.InternshipPortal.utils;

import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class AdminInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.username}")
    String username;

    @Value("${admin.password}")
    String password;

    @Value("${admin.phone}")
    Long phone;


    @Override
    public void run(String... args) {
        try {
            boolean exists = usersRepository.existsByUserEmail(email);

            if (!exists) {
                Users admin = new Users();
                admin.setUserEmail(email);
                admin.setUserPassword(passwordEncoder.encode(password));
                admin.setUserName(username);
                admin.setUserPhoneNumber(phone);
                admin.addRole("ROLE_ADMIN");
                usersRepository.save(admin);
                log.info("User {} created", username);
            } else {
                log.info("User {} already exists", username);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }
}
