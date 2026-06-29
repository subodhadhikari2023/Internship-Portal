package com.subodh.InternshipPortal.repositories;

import com.subodh.InternshipPortal.modals.Roles;
import com.subodh.InternshipPortal.modals.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link UsersRepository} using an in-memory H2 database.
 *
 * <p>Covers custom query methods: email lookup, userId lookup,
 * role-based filtering, and existence check.
 */
@DataJpaTest
@ActiveProfiles("test")
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    private Users student;
    private Users instructor;

    /**
     * Sets up a student and an instructor user before each test.
     */
    @BeforeEach
    void setUp() {
        usersRepository.deleteAll();

        student = new Users();
        student.setUserName("Test Student");
        student.setUserEmail("student@test.com");
        student.setUserPassword("encodedPassword");
        student.setUserPhoneNumber(9876543210L);
        student.addRole("ROLE_STUDENT");
        usersRepository.save(student);

        instructor = new Users();
        instructor.setUserName("Test Instructor");
        instructor.setUserEmail("instructor@test.com");
        instructor.setUserPassword("encodedPassword");
        instructor.setUserPhoneNumber(9876543211L);
        instructor.addRole("ROLE_INSTRUCTOR");
        usersRepository.save(instructor);
    }

    /**
     * Tests that {@link UsersRepository#findByUserEmail} returns the correct user.
     */
    @Test
    @DisplayName("findByUserEmail returns user when email matches")
    void findByUserEmail_returnsUser_whenEmailExists() {
        Users found = usersRepository.findByUserEmail("student@test.com");
        assertThat(found).isNotNull();
        assertThat(found.getUserName()).isEqualTo("Test Student");
    }

    /**
     * Tests that {@link UsersRepository#findByUserEmail} returns null for unknown email.
     */
    @Test
    @DisplayName("findByUserEmail returns null when email does not exist")
    void findByUserEmail_returnsNull_whenEmailDoesNotExist() {
        Users found = usersRepository.findByUserEmail("nobody@test.com");
        assertThat(found).isNull();
    }

    /**
     * Tests that {@link UsersRepository#findByUserId} returns the correct user.
     */
    @Test
    @DisplayName("findByUserId returns user when id matches")
    void findByUserId_returnsUser_whenIdExists() {
        Users saved = usersRepository.findByUserEmail("student@test.com");
        Users found = usersRepository.findByUserId(saved.getUserId());
        assertThat(found).isNotNull();
        assertThat(found.getUserEmail()).isEqualTo("student@test.com");
    }

    /**
     * Tests that {@link UsersRepository#findAllByRoleName} returns only users with the specified role.
     */
    @Test
    @DisplayName("findAllByRoleName returns only users with that role")
    void findAllByRoleName_returnsOnlyMatchingRole() {
        List<Users> students = usersRepository.findAllByRoleName("ROLE_STUDENT");
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getUserEmail()).isEqualTo("student@test.com");

        List<Users> instructors = usersRepository.findAllByRoleName("ROLE_INSTRUCTOR");
        assertThat(instructors).hasSize(1);
        assertThat(instructors.get(0).getUserEmail()).isEqualTo("instructor@test.com");
    }

    /**
     * Tests that {@link UsersRepository#existsByUserEmail} returns true for existing email.
     */
    @Test
    @DisplayName("existsByUserEmail returns true when email exists")
    void existsByUserEmail_returnsTrue_whenEmailExists() {
        assertThat(usersRepository.existsByUserEmail("student@test.com")).isTrue();
    }

    /**
     * Tests that {@link UsersRepository#existsByUserEmail} returns false for unknown email.
     */
    @Test
    @DisplayName("existsByUserEmail returns false when email does not exist")
    void existsByUserEmail_returnsFalse_whenEmailDoesNotExist() {
        assertThat(usersRepository.existsByUserEmail("ghost@test.com")).isFalse();
    }
}
