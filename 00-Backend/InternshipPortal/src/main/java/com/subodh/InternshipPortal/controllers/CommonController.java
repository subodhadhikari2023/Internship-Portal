package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.wrapper.UserWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/common")
public class CommonController {


    private final UsersRepository usersRepository;

    public CommonController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("get-profile-details")
    public ResponseEntity<?> getProfileDetails(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = usersRepository.findByUserEmail(userDetails.getUsername());
        UserWrapper userWrapper = new UserWrapper(user);
        return new ResponseEntity<>(userWrapper, HttpStatus.OK);
    }
}
