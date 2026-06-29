package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.exceptions.ApplicationCreationFailedException;
import com.subodh.InternshipPortal.modals.*;
import com.subodh.InternshipPortal.repositories.*;
import com.subodh.InternshipPortal.services.Implementation.ApplicationServiceImpl;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;
import com.subodh.InternshipPortal.wrapper.InternshipWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ApplicationServiceImpl}.
 *
 * <p>Verifies application submission, duplicate detection, review workflow,
 * and query methods, with all dependencies mocked.
 */
@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private InternshipRepository internshipRepository;

    @Mock
    private InternshipService internshipService;

    @Mock
    private InternshipStudentRepository internshipStudentRepository;

    @Mock
    private MailService mailService;

    @Mock
    private StudentApplicationRepository studentApplicationRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private Users student;
    private Users instructor;
    private Internship internship;
    private DepartmentDetails department;

    /**
     * Sets up shared test fixtures and injects the file storage path.
     */
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(applicationService, "rootFolderPath", "/tmp/test");

        department = new DepartmentDetails();
        department.setDepartmentName("IT");

        instructor = new Users();
        instructor.setUserId(2L);
        instructor.setUserEmail("instructor@test.com");
        instructor.setDepartment(department);

        student = new Users();
        student.setUserId(1L);
        student.setUserEmail("student@test.com");
        student.setUserName("Test Student");
        student.addRole("ROLE_STUDENT");

        internship = new Internship();
        internship.setInternshipId(10L);
        internship.setInternshipName("Java Internship");
        internship.setCreatedBy(instructor);
        internship.setDepartment(department);
    }

    /**
     * Tests that {@code save} persists an application when no prior application exists.
     */
    @Test
    @DisplayName("save persists application when no duplicate exists")
    void save_persistsApplication_whenNoDuplicate() {
        Application application = new Application();
        application.setInternship(internship);

        when(usersRepository.findByUserEmail("student@test.com")).thenReturn(student);
        when(applicationRepository.existsByInternshipAndStudent(10L, 1L)).thenReturn(Optional.empty());
        when(internshipRepository.findByInternshipId(10L)).thenReturn(internship);
        when(applicationRepository.save(any())).thenReturn(application);
        when(studentApplicationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ApplicationWrapper result = applicationService.save(application, "student@test.com");
        assertThat(result).isNotNull();
        verify(applicationRepository).save(application);
    }

    /**
     * Tests that {@code save} throws {@link ApplicationCreationFailedException}
     * when the student has already applied.
     */
    @Test
    @DisplayName("save throws ApplicationCreationFailedException when duplicate application exists")
    void save_throwsException_whenDuplicateApplicationExists() {
        Application application = new Application();
        application.setInternship(internship);

        when(usersRepository.findByUserEmail("student@test.com")).thenReturn(student);
        Application existingApp = new Application();
        existingApp.setInternship(internship);
        existingApp.setStudent(student);
        ApplicationWrapper existingWrapper = new ApplicationWrapper(existingApp);
        when(applicationRepository.existsByInternshipAndStudent(10L, 1L))
                .thenReturn(Optional.of(existingWrapper));

        assertThatThrownBy(() -> applicationService.save(application, "student@test.com"))
                .isInstanceOf(ApplicationCreationFailedException.class)
                .hasMessageContaining("already applied");
    }

    /**
     * Tests that {@code reviewApplications} with ACCEPTED status creates an {@link InternshipStudents} entry.
     */
    @Test
    @DisplayName("reviewApplications ACCEPTED creates InternshipStudents entry")
    void reviewApplications_ACCEPTED_createsInternshipStudentsEntry() {
        Application application = new Application();
        application.setStudent(student);
        application.setInternship(internship);

        StudentApplication studentApplication = new StudentApplication(application);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(studentApplicationRepository.findByApplicationId(1L)).thenReturn(studentApplication);
        when(studentApplicationRepository.save(any())).thenReturn(studentApplication);

        applicationService.reviewApplications(StudentApplicationStatus.ACCEPTED, 1L);

        verify(internshipStudentRepository).save(any(InternshipStudents.class));
        verify(applicationRepository).delete(application);
    }

    /**
     * Tests that {@code reviewApplications} with REJECTED status does not create an internship student entry.
     */
    @Test
    @DisplayName("reviewApplications REJECTED does not create InternshipStudents entry")
    void reviewApplications_REJECTED_doesNotCreateInternshipStudentsEntry() {
        Application application = new Application();
        application.setStudent(student);
        application.setInternship(internship);

        StudentApplication studentApplication = new StudentApplication(application);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(studentApplicationRepository.findByApplicationId(1L)).thenReturn(studentApplication);
        when(studentApplicationRepository.save(any())).thenReturn(studentApplication);

        applicationService.reviewApplications(StudentApplicationStatus.REJECTED, 1L);

        verify(internshipStudentRepository, never()).save(any());
        verify(applicationRepository).delete(application);
    }

    /**
     * Tests that {@code findAllApplicationsByUserEmail} returns wrapped applications for the student.
     */
    @Test
    @DisplayName("findAllApplicationsByUserEmail returns student's applications")
    void findAllApplicationsByUserEmail_returnsApplications() {
        Application application = new Application();
        application.setStudent(student);
        application.setInternship(internship);
        StudentApplication studentApplication = new StudentApplication(application);

        when(usersRepository.findByUserEmail("student@test.com")).thenReturn(student);
        when(studentApplicationRepository.findAllByStudent(student)).thenReturn(List.of(studentApplication));

        List<ApplicationWrapper> result = applicationService.findAllApplicationsByUserEmail("student@test.com");
        assertThat(result).hasSize(1);
    }
}
