package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.exceptions.DuplicateDepartmentException;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.repositories.DepartmentRepository;
import com.subodh.InternshipPortal.services.DepartmentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Department service.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    /**
     * Instantiates a new Department service.
     *
     * @param departmentRepository the department repository
     */
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

        DepartmentDetails departmentDetails = new DepartmentDetails();
        departmentDetails.setDepartmentName(departmentName);
        try {
            return departmentRepository.save(departmentDetails);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDepartmentException("Department already exists: " + departmentName);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create department: " + departmentName, e);
        }
    }
}
