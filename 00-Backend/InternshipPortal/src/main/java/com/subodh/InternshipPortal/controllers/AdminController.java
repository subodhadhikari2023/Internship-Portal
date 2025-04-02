package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.services.DepartmentService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.InstructorWrapper;
import com.subodh.InternshipPortal.wrapper.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/v1/administrator")
public class AdminController {

    private final DepartmentService departmentService;
    private final UserService userService;

    public AdminController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }


    @GetMapping("department")
    public ResponseEntity<?> getAllDepartment() {
        return Optional.of(departmentService.findAllDepartments())
                .filter(departments -> !departments.isEmpty())
                .map(departments -> new ResponseEntity<>(new Response<>(departments), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new Response<>(Collections.emptyList()), HttpStatus.NO_CONTENT));

    }

    @PostMapping("department")
    public ResponseEntity<?> addDepartment(@RequestBody APIRequest<String> departmentName) {
        return new ResponseEntity<>(new Response<>(departmentService.createDepartment(departmentName.getEntity())), HttpStatus.OK);
    }

    @GetMapping("users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(new Response<>(userService.findAllUsers()), HttpStatus.OK);
    }

    @PostMapping("users")
    public ResponseEntity<?> addUser(@RequestBody InstructorWrapper instructor) {
        return new ResponseEntity<>(new Response<>(userService.addInstructor(instructor)), HttpStatus.CREATED);

    }
    @GetMapping("students")
    public ResponseEntity<?> getAllStudents() {
        return new ResponseEntity<>(new Response<>(userService.findAllStudents()),HttpStatus.OK);
    }
}










