package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.wrapper.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/v1/administrator")
public class AdminController {

    public AdminController() {}

    @GetMapping
    public ResponseEntity<?> getAdmin() {
        return new ResponseEntity<>(new Response<>("Admin controller works"), HttpStatus.OK);
    }
}
