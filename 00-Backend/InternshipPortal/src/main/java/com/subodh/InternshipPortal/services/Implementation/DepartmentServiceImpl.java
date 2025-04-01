package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.repositories.DepartmentRepository;
import com.subodh.InternshipPortal.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<String> findAllDepartments() {
        List<DepartmentDetails> departmentDetails = departmentRepository.findAll();
        return departmentDetails.stream().map(DepartmentDetails::getDepartmentName).collect(Collectors.toList());
    }

    @Override
    public DepartmentDetails createDepartment(String departmentName) {
        try {
            DepartmentDetails departmentDetails = new DepartmentDetails();
            departmentDetails.setDepartmentName(departmentName);
            return departmentRepository.save(departmentDetails);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new RuntimeException("Department already exists " + departmentName);
            }
            throw new RuntimeException("Unable to create department " + departmentName);
        }
    }
}
