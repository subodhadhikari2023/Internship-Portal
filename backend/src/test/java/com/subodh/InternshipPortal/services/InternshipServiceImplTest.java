package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.enums.InternshipStatus;
import com.subodh.InternshipPortal.enums.WorkMode;
import com.subodh.InternshipPortal.exceptions.InternshipCreationFailedException;
import com.subodh.InternshipPortal.modals.DepartmentDetails;
import com.subodh.InternshipPortal.modals.Internship;
import com.subodh.InternshipPortal.modals.StudentApplication;
import com.subodh.InternshipPortal.modals.Users;
import com.subodh.InternshipPortal.repositories.InternshipRepository;
import com.subodh.InternshipPortal.repositories.StudentApplicationRepository;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import com.subodh.InternshipPortal.services.Implementation.InternshipServiceImpl;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link InternshipServiceImpl}.
 *
 * <p>All repository and context interactions are mocked.
 */
@ExtendWith(MockitoExtension.class)
class InternshipServiceImplTest {

    @Mock
    private InternshipRepository internshipRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private StudentApplicationRepository studentApplicationRepository;

    @InjectMocks
    private InternshipServiceImpl internshipService;

    private Users instructor;
    private DepartmentDetails department;
    private Internship internship;

    /**
     * Sets up shared test fixtures and clears the security context.
     */
    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        department = new DepartmentDetails();
        department.setDepartmentName("IT");

        instructor = new Users();
        instructor.setUserId(1L);
        instructor.setUserEmail("instructor@test.com");
        instructor.setUserName("Instructor");
        instructor.setDepartment(department);

        internship = new Internship();
        internship.setInternshipId(10L);
        internship.setInternshipName("Java Internship");
        internship.setStartDate(LocalDate.now());
        internship.setEndDate(LocalDate.now().plusMonths(3));
        internship.setStatus(InternshipStatus.ACTIVE);
        internship.setWorkMode(WorkMode.REMOTE);
        internship.setCreatedBy(instructor);
        internship.setDepartment(department);
        internship.setRequiredSkills(Set.of(" Java ", "Spring "));
    }

    /**
     * Populates the {@link SecurityContextHolder} with a mock instructor principal.
     */
    private void setUpSecurityContext() {
        UserDetails principal = User.withUsername("instructor@test.com")
                .password("pass")
                .authorities(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
                .build();
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
        SecurityContextHolder.setContext(ctx);
    }

    /**
     * Tests that {@code saveInternship} trims skills and delegates to the repository.
     */
    @Test
    @DisplayName("saveInternship persists internship and trims required skills")
    void saveInternship_persistsAndTrimsSkills() {
        when(usersRepository.findByUserEmail("instructor@test.com")).thenReturn(instructor);
        when(internshipRepository.save(any(Internship.class))).thenReturn(internship);

        InternshipWrapper result = internshipService.saveInternship(internship, "instructor@test.com");

        assertThat(result).isNotNull();
        assertThat(result.getInternshipName()).isEqualTo("Java Internship");
        verify(internshipRepository).save(internship);
    }

    /**
     * Tests that {@code saveInternship} throws {@link InternshipCreationFailedException}
     * on a {@link DataIntegrityViolationException}.
     */
    @Test
    @DisplayName("saveInternship throws InternshipCreationFailedException on data integrity violation")
    void saveInternship_throwsInternshipCreationFailedException_onDataIntegrityViolation() {
        when(usersRepository.findByUserEmail("instructor@test.com")).thenReturn(instructor);
        when(internshipRepository.save(any())).thenThrow(new DataIntegrityViolationException("dup"));

        assertThatThrownBy(() -> internshipService.saveInternship(internship, "instructor@test.com"))
                .isInstanceOf(InternshipCreationFailedException.class);
    }

    /**
     * Tests that {@code findAll} returns all internships when at least one ACTIVE exists.
     */
    @Test
    @DisplayName("findAll returns all internships when active ones exist")
    void findAll_returnsAll_whenActiveExists() {
        when(internshipRepository.findAllByStatus(InternshipStatus.ACTIVE)).thenReturn(List.of(internship));
        when(internshipRepository.findAll()).thenReturn(List.of(internship));

        List<InternshipWrapper> result = internshipService.findAll();
        assertThat(result).hasSize(1);
    }

    /**
     * Tests that {@code findAll} returns an empty list when no ACTIVE internships exist.
     */
    @Test
    @DisplayName("findAll returns empty list when no active internships exist")
    void findAll_returnsEmpty_whenNoActiveInternships() {
        when(internshipRepository.findAllByStatus(InternshipStatus.ACTIVE)).thenReturn(List.of());

        List<InternshipWrapper> result = internshipService.findAll();
        assertThat(result).isEmpty();
    }

    /**
     * Tests that {@code findAllByInstructor} returns internships created by the authenticated instructor.
     */
    @Test
    @DisplayName("findAllByInstructor returns internships for the authenticated instructor")
    void findAllByInstructor_returnsInternships_forAuthenticatedInstructor() {
        setUpSecurityContext();
        when(usersRepository.findByUserEmail("instructor@test.com")).thenReturn(instructor);
        when(internshipRepository.findAllByCreatedBy(instructor)).thenReturn(List.of(internship));

        List<InternshipWrapper> result = internshipService.findAllByInstructor();
        assertThat(result).hasSize(1);
    }

    /**
     * Tests that {@code findAllApplicationsbyCreatedBy} returns null when no applications exist.
     */
    @Test
    @DisplayName("findAllApplicationsbyCreatedBy returns null when list is empty")
    void findAllApplicationsbyCreatedBy_returnsNull_whenEmpty() {
        when(usersRepository.findByUserEmail("instructor@test.com")).thenReturn(instructor);
        when(studentApplicationRepository.findAllByInternship_CreatedBy(instructor)).thenReturn(List.of());

        List<StudentApplication> result = internshipService.findAllApplicationsbyCreatedBy("instructor@test.com");
        assertThat(result).isNull();
    }
}
