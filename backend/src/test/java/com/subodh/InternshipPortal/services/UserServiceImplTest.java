package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.exceptions.UserCreationException;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.DepartmentRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.Implementation.UserServiceImplementation;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.wrapper.InstructorWrapper;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.wrapper.StudentWrapper;
import com.subodh.InternshipPortal.wrapper.UserWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImplementation}.
 *
 * <p>All external dependencies are mocked with Mockito. File-system operations
 * are not exercised in these tests.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UsersRepository userRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AuthenticationManager auth;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private MailService mailService;

    private UserServiceImplementation userService;

    private Users student;
    private Users instructor;
    private DepartmentDetails department;

    /**
     * Initialises common test fixtures and constructs the service with explicit mock wiring,
     * since {@code UserServiceImplementation} takes two {@code UsersRepository} parameters of the
     * same type and Mockito cannot distinguish them without the {@code -parameters} compiler flag.
     */
    @BeforeEach
    void setUp() {
        userService = new UserServiceImplementation(
                userRepository, auth, passwordEncoder, jwtUtils,
                registrationService, usersRepository,
                departmentRepository, mailService
        );

        department = new DepartmentDetails();
        department.setDepartmentName("IT");

        student = new Users();
        student.setUserId(1L);
        student.setUserEmail("student@test.com");
        student.setUserName("Student One");
        student.setUserPassword("encodedPassword");
        student.addRole("ROLE_STUDENT");

        instructor = new Users();
        instructor.setUserId(2L);
        instructor.setUserEmail("instructor@test.com");
        instructor.setUserName("Instructor One");
        instructor.setUserPassword("encodedPassword");
        instructor.setDepartment(department);
        instructor.addRole("ROLE_INSTRUCTOR");
    }

    /**
     * Tests that {@code saveUser} encodes the password and assigns the STUDENT role.
     */
    @Test
    @DisplayName("saveUser encodes password and assigns ROLE_STUDENT")
    void saveUser_encodesPasswordAndAssignsStudentRole() {
        RegistrationEntity reg = new RegistrationEntity();
        reg.setUserEmail("newstudent@test.com");
        reg.setUserName("New Student");
        reg.setUserPassword("plainPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(Users.class))).thenAnswer(inv -> inv.getArgument(0));

        Users saved = userService.saveUser(reg);
        assertThat(saved.getUserPassword()).isEqualTo("encodedPass");
        assertThat(saved.getRoles()).anyMatch(r -> r.getRoleName().equals("ROLE_STUDENT"));
        verify(registrationService).delete(reg);
    }

    /**
     * Tests that {@code emailExists} returns {@code true} when the user is found.
     */
    @Test
    @DisplayName("emailExists returns true when user with given email exists")
    void emailExists_returnsTrue_whenUserFound() {
        when(userRepository.findByUserEmail("student@test.com")).thenReturn(student);
        assertThat(userService.emailExists("student@test.com")).isTrue();
    }

    /**
     * Tests that {@code emailExists} returns {@code false} when no user is found.
     */
    @Test
    @DisplayName("emailExists returns false when user not found")
    void emailExists_returnsFalse_whenUserNotFound() {
        assertThat(userService.emailExists("nobody@test.com")).isFalse();
    }

    /**
     * Tests that {@code findByUserEmail} delegates directly to the repository.
     */
    @Test
    @DisplayName("findByUserEmail delegates to repository")
    void findByUserEmail_delegatesToRepository() {
        when(userRepository.findByUserEmail("student@test.com")).thenReturn(student);
        Users result = userService.findByUserEmail("student@test.com");
        assertThat(result).isEqualTo(student);
    }

    /**
     * Tests that {@code verifyUserCredentials} returns a JWT token on successful authentication.
     */
    @Test
    @DisplayName("verifyUserCredentials returns JWT token on successful login")
    void verifyUserCredentials_returnsToken_onSuccess() throws Exception {
        Users loginUser = new Users();
        loginUser.setUserEmail("student@test.com");
        loginUser.setUserPassword("password");

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(auth.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtUtils.generateToken(mockAuth)).thenReturn("jwt.token.here");

        String token = userService.verifyUserCredentials(loginUser);
        assertThat(token).isEqualTo("jwt.token.here");
    }

    /**
     * Tests that {@code findAllStudents} returns only users with ROLE_STUDENT.
     */
    @Test
    @DisplayName("findAllStudents returns only students")
    void findAllStudents_returnsOnlyStudents() {
        when(usersRepository.findAllByRoleName("ROLE_STUDENT")).thenReturn(List.of(student));

        List<StudentWrapper> students = userService.findAllStudents();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getUserEmail()).isEqualTo("student@test.com");
    }

    /**
     * Tests that {@code findAllInstructors} returns only users with ROLE_INSTRUCTOR.
     */
    @Test
    @DisplayName("findAllInstructors returns only instructors")
    void findAllInstructors_returnsOnlyInstructors() {
        when(usersRepository.findAllByRoleName("ROLE_INSTRUCTOR")).thenReturn(List.of(instructor));

        List<InstructorWrapper> instructors = userService.findAllInstructors();
        assertThat(instructors).hasSize(1);
        assertThat(instructors.get(0).getUserEmail()).isEqualTo("instructor@test.com");
    }

    /**
     * Tests that {@code forgetPassword} returns a {@link UserWrapper} when the user is found.
     */
    @Test
    @DisplayName("forgetPassword returns UserWrapper when user found")
    void forgetPassword_returnsUserWrapper_whenUserFound() {
        when(userRepository.findByUserEmail("student@test.com")).thenReturn(student);

        UserWrapper result = userService.forgetPassword("student@test.com");
        assertThat(result).isNotNull();
    }

    /**
     * Tests that {@code forgetPassword} returns {@code null} when no user is found.
     */
    @Test
    @DisplayName("forgetPassword returns null when user not found")
    void forgetPassword_returnsNull_whenUserNotFound() {
        UserWrapper result = userService.forgetPassword("nobody@test.com");
        assertThat(result).isNull();
    }

    /**
     * Tests that {@code resetPasswordUserFoundByEmail} encodes and saves the new password.
     */
    @Test
    @DisplayName("resetPasswordUserFoundByEmail encodes and saves new password")
    void resetPasswordUserFoundByEmail_encodesAndSaves() {
        when(userRepository.findByUserEmail("student@test.com")).thenReturn(student);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(student)).thenReturn(student);

        UserWrapper result = userService.resetPasswordUserFoundByEmail("student@test.com", "newPass");
        assertThat(result).isNotNull();
        assertThat(student.getUserPassword()).isEqualTo("encodedNewPass");
    }

    /**
     * Tests that {@code addInstructor} throws {@link UserCreationException} on a duplicate email.
     */
    @Test
    @DisplayName("addInstructor throws UserCreationException on duplicate email")
    void addInstructor_throwsUserCreationException_onDuplicate() {
        InstructorWrapper wrapper = new InstructorWrapper();
        wrapper.setUserName("Dup Instructor");
        wrapper.setUserEmail("dup@test.com");
        wrapper.setPhoneNumber(9876543210L);
        wrapper.setDepartment("IT");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(departmentRepository.findByDepartmentName("IT")).thenReturn(Optional.of(department));
        when(usersRepository.save(any())).thenThrow(new DataIntegrityViolationException("dup"));

        assertThatThrownBy(() -> userService.addInstructor(wrapper))
                .isInstanceOf(UserCreationException.class);
    }

    /**
     * Tests that {@code validatePassword} returns {@code true} when the password matches.
     */
    @Test
    @DisplayName("validatePassword returns true when password matches")
    void validatePassword_returnsTrue_whenMatches() {
        when(userRepository.findByUserEmail("student@test.com")).thenReturn(student);
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        assertThat(userService.validatePassword("student@test.com", "plainPassword")).isTrue();
    }
}
