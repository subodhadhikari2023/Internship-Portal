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

/**
 * The type Admin controller.
 */
@CrossOrigin
@RestController
@RequestMapping("api/v1/administrator")
public class AdminController {

    private final DepartmentService departmentService;
    private final UserService userService;

    /**
     * Instantiates a new Admin controller.
     *
     * @param departmentService the department service
     * @param userService       the user service
     */
    public AdminController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }


    /**
     * Gets all department.
     *
     * @return the all department
     */
    @GetMapping("department")
    public ResponseEntity<?> getAllDepartment() {
        return Optional.of(departmentService.findAllDepartments())
                .filter(departments -> !departments.isEmpty())
                .map(departments -> new ResponseEntity<>(new Response<>(departments), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new Response<>(Collections.emptyList()), HttpStatus.NO_CONTENT));

    }

    /**
     * Add department response entity.
     *
     * @param departmentName the department name
     * @return the response entity
     */
    @PostMapping("department")
    public ResponseEntity<?> addDepartment(@RequestBody APIRequest<String> departmentName) {
        return new ResponseEntity<>(new Response<>(departmentService.createDepartment(departmentName.getEntity())), HttpStatus.OK);
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    @GetMapping("users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(new Response<>(userService.findAllUsers()), HttpStatus.OK);
    }

    /**
     * Gets all instructors.
     *
     * @return the all instructors
     */
    @GetMapping("instructors")
    public ResponseEntity<?> getAllInstructors() {
        return new ResponseEntity<>(new Response<>(userService.findAllInstructors()), HttpStatus.OK);
    }

    /**
     * Add user response entity.
     *
     * @param instructor the instructor
     * @return the response entity
     */
    @PostMapping("instructors")
    public ResponseEntity<?> addUser(@RequestBody InstructorWrapper instructor) {
        return new ResponseEntity<>(new Response<>(userService.addInstructor(instructor)), HttpStatus.CREATED);

    }

    /**
     * Gets all students.
     *
     * @return the all students
     */
    @GetMapping("students")
    public ResponseEntity<?> getAllStudents() {
        return new ResponseEntity<>(new Response<>(userService.findAllStudents()), HttpStatus.OK);
    }
}










