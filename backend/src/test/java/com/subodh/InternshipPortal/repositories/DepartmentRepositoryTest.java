package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.DepartmentDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link DepartmentRepository} using an in-memory H2 database.
 *
 * <p>Covers {@code findByDepartmentName} and basic CRUD behaviour.
 */
@DataJpaTest
@ActiveProfiles("test")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Clears the repository before each test to ensure isolation.
     */
    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
    }

    /**
     * Tests that a saved department can be retrieved by name.
     */
    @Test
    @DisplayName("findByDepartmentName returns department when name matches")
    void findByDepartmentName_returnsPresent_whenNameExists() {
        DepartmentDetails dept = new DepartmentDetails();
        dept.setDepartmentName("Information Technology");
        departmentRepository.save(dept);

        Optional<DepartmentDetails> found = departmentRepository.findByDepartmentName("Information Technology");
        assertThat(found).isPresent();
        assertThat(found.get().getDepartmentName()).isEqualTo("Information Technology");
    }

    /**
     * Tests that querying an unknown department name returns empty.
     */
    @Test
    @DisplayName("findByDepartmentName returns empty when name does not exist")
    void findByDepartmentName_returnsEmpty_whenNameDoesNotExist() {
        Optional<DepartmentDetails> found = departmentRepository.findByDepartmentName("NonExistent");
        assertThat(found).isEmpty();
    }

    /**
     * Tests that {@code findAll} returns all persisted departments.
     */
    @Test
    @DisplayName("findAll returns all saved departments")
    void findAll_returnsAllDepartments() {
        DepartmentDetails dept1 = new DepartmentDetails();
        dept1.setDepartmentName("IT");
        DepartmentDetails dept2 = new DepartmentDetails();
        dept2.setDepartmentName("Finance");
        departmentRepository.save(dept1);
        departmentRepository.save(dept2);

        List<DepartmentDetails> all = departmentRepository.findAll();
        assertThat(all).hasSize(2);
    }
}
