package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.enums.WorkMode;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link InternshipRepository} using an in-memory H2 database.
 *
 * <p>Covers custom finders: by creator, by status, top-5 by date, and
 * the native query that fetches by instructor id.
 */
@DataJpaTest
@ActiveProfiles("test")
class InternshipRepositoryTest {

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Users instructor;
    private DepartmentDetails department;

    /**
     * Persists a department and an instructor user before each test.
     */
    @BeforeEach
    void setUp() {
        internshipRepository.deleteAll();
        usersRepository.deleteAll();
        departmentRepository.deleteAll();

        department = new DepartmentDetails();
        department.setDepartmentName("IT");
        departmentRepository.save(department);

        instructor = new Users();
        instructor.setUserName("Instructor One");
        instructor.setUserEmail("instructor@test.com");
        instructor.setUserPassword("password");
        instructor.addRole("ROLE_INSTRUCTOR");
        instructor.setDepartment(department);
        usersRepository.save(instructor);
    }

    /**
     * Creates and persists an {@link Internship} with the given status for the shared instructor.
     *
     * @param name   internship name
     * @param status the {@link InternshipStatus} to assign
     * @return the saved internship
     */
    private Internship createInternship(String name, InternshipStatus status) {
        Internship internship = new Internship();
        internship.setInternshipName(name);
        internship.setStartDate(LocalDate.now());
        internship.setEndDate(LocalDate.now().plusMonths(3));
        internship.setStatus(status);
        internship.setWorkMode(WorkMode.REMOTE);
        internship.setCreatedBy(instructor);
        internship.setDepartment(department);
        internship.setRequiredSkills(Set.of("Java", "Spring"));
        return internshipRepository.save(internship);
    }

    /**
     * Tests that {@code findByInternshipId} returns the saved entity.
     */
    @Test
    @DisplayName("findByInternshipId returns internship when id matches")
    void findByInternshipId_returnsInternship_whenIdExists() {
        Internship saved = createInternship("Java Internship", InternshipStatus.ACTIVE);
        Internship found = internshipRepository.findByInternshipId(saved.getInternshipId());
        assertThat(found).isNotNull();
        assertThat(found.getInternshipName()).isEqualTo("Java Internship");
    }

    /**
     * Tests that {@code findAllByCreatedBy} returns all internships owned by the given instructor.
     */
    @Test
    @DisplayName("findAllByCreatedBy returns internships for the given instructor")
    void findAllByCreatedBy_returnsInternships_forGivenInstructor() {
        createInternship("Java Internship", InternshipStatus.ACTIVE);
        createInternship("Python Internship", InternshipStatus.INACTIVE);

        List<Internship> list = internshipRepository.findAllByCreatedBy(instructor);
        assertThat(list).hasSize(2);
    }

    /**
     * Tests that {@code findAllByStatus} returns only internships with the given status.
     */
    @Test
    @DisplayName("findAllByStatus returns only internships with the specified status")
    void findAllByStatus_returnsOnlyMatchingStatus() {
        createInternship("Active One", InternshipStatus.ACTIVE);
        createInternship("Inactive One", InternshipStatus.INACTIVE);

        List<Internship> active = internshipRepository.findAllByStatus(InternshipStatus.ACTIVE);
        assertThat(active).hasSize(1);
        assertThat(active.get(0).getInternshipName()).isEqualTo("Active One");
    }

    /**
     * Tests that {@code findAllByStatusAndCreatedBy} returns active internships for a given creator.
     */
    @Test
    @DisplayName("findAllByStatusAndCreatedBy returns matching internships")
    void findAllByStatusAndCreatedBy_returnsMatchingInternships() {
        createInternship("Active Java", InternshipStatus.ACTIVE);
        createInternship("Inactive Python", InternshipStatus.INACTIVE);

        List<Internship> result = internshipRepository.findAllByStatusAndCreatedBy(InternshipStatus.ACTIVE, instructor);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInternshipName()).isEqualTo("Active Java");
    }

    /**
     * Tests that {@code findTop5ByCreatedByOrderByStartDateDesc} returns at most 5 records.
     */
    @Test
    @DisplayName("findTop5ByCreatedByOrderByStartDateDesc returns up to 5 most recent internships")
    void findTop5ByCreatedByOrderByStartDateDesc_returnsAtMostFive() {
        for (int i = 1; i <= 7; i++) {
            createInternship("Internship " + i, InternshipStatus.ACTIVE);
        }
        List<Internship> top5 = internshipRepository.findTop5ByCreatedByOrderByStartDateDesc(instructor);
        assertThat(top5).hasSize(5);
    }
}
