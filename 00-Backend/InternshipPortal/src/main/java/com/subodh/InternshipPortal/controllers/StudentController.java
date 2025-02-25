package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.entities.Internship;
import com.subodh.InternshipPortal.services.InternshipService;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private final InternshipService internshipService;

    public StudentController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @GetMapping("view-internships")
    public ResponseEntity<?> viewInternships() {
        List<InternshipWrapper> internshipList = internshipService.findAll();
        return new ResponseEntity<>(internshipList, HttpStatus.OK);
    }


}
