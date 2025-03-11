package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.entities.DepartmentDetails;
import com.subodh.InternshipPortal.repositories.DepartmentRepository;
import com.subodh.InternshipPortal.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<String> findAll() {
        List<DepartmentDetails> departmentDetails = departmentRepository.findAll();
       return departmentDetails.stream().map(DepartmentDetails::getDepartmentName).collect(Collectors.toList());
    }
}
