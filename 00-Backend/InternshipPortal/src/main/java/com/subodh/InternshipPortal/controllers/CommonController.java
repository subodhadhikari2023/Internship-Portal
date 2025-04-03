package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/common")
public class CommonController {


    private final UserService userService;

    public CommonController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        userService.forgetPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
