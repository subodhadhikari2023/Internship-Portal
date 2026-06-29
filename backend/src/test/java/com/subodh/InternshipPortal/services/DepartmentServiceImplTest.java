package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.exceptions.DuplicateDepartmentException;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.repositories.DepartmentRepository;
import com.subodh.InternshipPortal.services.Implementation.DepartmentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DepartmentServiceImpl}.
 *
 * <p>All repository interactions are mocked with Mockito.
 */
@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    /**
     * Tests that {@code findAllDepartments} maps persisted entities to names.
     */
    @Test
    @DisplayName("findAllDepartments returns list of department names")
    void findAllDepartments_returnsNames() {
        DepartmentDetails dept1 = new DepartmentDetails();
        dept1.setDepartmentName("IT");
        DepartmentDetails dept2 = new DepartmentDetails();
        dept2.setDepartmentName("Finance");

        when(departmentRepository.findAll()).thenReturn(List.of(dept1, dept2));

        List<String> result = departmentService.findAllDepartments();
        assertThat(result).containsExactlyInAnyOrder("IT", "Finance");
    }

    /**
     * Tests that {@code createDepartment} returns the saved entity on success.
     */
    @Test
    @DisplayName("createDepartment returns saved DepartmentDetails")
    void createDepartment_returnsSaved_onSuccess() {
        DepartmentDetails dept = new DepartmentDetails();
        dept.setDepartmentName("IT");
        when(departmentRepository.save(any(DepartmentDetails.class))).thenReturn(dept);

        DepartmentDetails result = departmentService.createDepartment("IT");
        assertThat(result.getDepartmentName()).isEqualTo("IT");
    }

    /**
     * Tests that {@code createDepartment} throws {@link DuplicateDepartmentException}
     * when a duplicate name causes a DB constraint violation.
     */
    @Test
    @DisplayName("createDepartment throws DuplicateDepartmentException on duplicate name")
    void createDepartment_throwsDuplicateDepartmentException_onDuplicate() {
        when(departmentRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> departmentService.createDepartment("IT"))
                .isInstanceOf(DuplicateDepartmentException.class)
                .hasMessageContaining("IT");
    }
}
