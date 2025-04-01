package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.services.DepartmentService;
import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/v1/administrator")
public class AdminController {

    private final DepartmentService departmentService;

    public AdminController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAdmin() {
        return new ResponseEntity<>(new Response<>("Admin controller works"), HttpStatus.OK);
    }

    @GetMapping("department")
    public ResponseEntity<?> getAllDepartment() {
        return Optional.of(departmentService.findAll())
                .filter(departments -> !departments.isEmpty())
                .map(departments -> new ResponseEntity<>(new Response<>(departments), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new Response<>(Collections.emptyList()), HttpStatus.FORBIDDEN));

    }

    @PostMapping("department")
    public ResponseEntity<?> addDepartment(@RequestBody APIRequest<String> departmentName) {
        log.info("Add department to admin controller");
        return new ResponseEntity<>(new Response<>(departmentService.createDepartment(departmentName.getEntity())), HttpStatus.OK);
    }
}










