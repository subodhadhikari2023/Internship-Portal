package com.subodh.InternshipPortal.services.Implementation;


import com.subodh.InternshipPortal.exceptions.UserCreationException;
import com.subodh.InternshipPortal.modals.*;
import com.subodh.InternshipPortal.repositories.DepartmentRepository;
import com.subodh.InternshipPortal.wrapper.InstructorWrapper;
import com.subodh.InternshipPortal.wrapper.RegistrationEntity;
import com.subodh.InternshipPortal.repositories.UsersRepository;

import com.subodh.InternshipPortal.services.RegistrationService;
import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.wrapper.StudentWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The type User service implementation.
 */
@Slf4j
@Service
public class UserServiceImplementation implements UserService {

    private final UsersRepository userRepository;
    private final AuthenticationManager auth;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RegistrationService registrationService;
    private final UsersRepository usersRepository;
    private final DepartmentRepository departmentRepository;

    @Value("${file.storage.path}")
    private String rootFolderPath;


    /**
     * Instantiates a new User service implementation.
     *
     * @param userRepository      the user repository
     * @param auth                the auth
     * @param passwordEncoder     the password encoder
     * @param jwtUtils            the jwt utils
     * @param registrationService the registration service
     */
    @Autowired
    public UserServiceImplementation(UsersRepository userRepository, AuthenticationManager auth, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RegistrationService registrationService, UsersRepository usersRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.auth = auth;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.registrationService = registrationService;
        this.usersRepository = usersRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public Users saveUser(RegistrationEntity tempUser) {
        Users user = new Users();
        user.setUserEmail(tempUser.getUserEmail());
        user.setUserPassword(tempUser.getUserPassword());
        user.setUserName(tempUser.getUserName());
        user.setUserPhoneNumber(tempUser.getUserPhoneNumber());
        user.addRole("ROLE_STUDENT");
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        registrationService.delete(tempUser);
        return userRepository.save(user);
    }

    @Override

    public String verifyUserCredentials(Users user) {
        Authentication authenticate = auth.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword()));
        boolean authenticated = authenticate.isAuthenticated();
        if (authenticated)
            return jwtUtils.generateToken(authenticate);
        throw new RuntimeException("Invalid user credentials");
    }

    @Override
    public List<?> findAllUsers() {


        return userRepository.findAll().stream()
                .map(user -> {
                    boolean isInstructor = user.getRoles().stream()
                            .anyMatch(role -> role.getRoleName().equalsIgnoreCase("ROLE_INSTRUCTOR"));
                    boolean isStudent = user.getRoles().stream()
                            .anyMatch(role -> role.getRoleName().equalsIgnoreCase("ROLE_STUDENT"));

                    if (isInstructor) {
                        return new InstructorWrapper(user);
                    } else if (isStudent) {
                        return new StudentWrapper(user);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean verifyOtp(Users user, Long otp) {
        return true;
    }

    @Override
    public boolean emailExists(String userEmail) {
        return userRepository.findByUserEmail(userEmail) != null;
    }

    @Override
    public Users findByUserEmail(String username) {
        return userRepository.findByUserEmail(username);
    }

    @Override
    @Transactional
    public StudentWrapper updateStudent(UserDetails userDetails, StudentWrapper userWrapper) {
        Users user = userRepository.findByUserEmail(userDetails.getUsername());
        log.info("Updating user {}", user.getUserEmail());
        // Ensure education object exists
        user.setEducation(Optional.ofNullable(user.getEducation()).orElse(new Education()));

        // Update user details
        updateIfNotNull(userWrapper.getUserName(), user::setUserName);
        updateIfNotNull(userWrapper.getPhoneNumber(), user::setUserPhoneNumber);
        updateIfNotNull(userWrapper.getSkills(), user::setStudentSkills);

        // Update education details
        updateEducationDetails(user, userWrapper);

        return new StudentWrapper(userRepository.save(user));
    }


    private void updateEducationDetails(Users user, StudentWrapper userWrapper) {
        // Initialize education objects if null
        Education education = user.getEducation();
        education.setSecondaryEducation(Optional.ofNullable(education.getSecondaryEducation()).orElse(new SecondaryEducation()));
        education.setHigherSecondaryEducation(Optional.ofNullable(education.getHigherSecondaryEducation()).orElse(new HigherSecondaryEducation()));
        education.setDiplomaEducation(Optional.ofNullable(education.getDiplomaEducation()).orElse(new DiplomaEducation()));
        education.setBachelorsEducation(Optional.ofNullable(education.getBachelorsEducation()).orElse(new BachelorsEducation()));
        education.setMastersEducation(Optional.ofNullable(education.getMastersEducation()).orElse(new MastersEducation()));

        // Update fields using helper function
        SecondaryEducation secEdu = education.getSecondaryEducation();
        updateIfNotNull(userWrapper.getSecondarySchoolName(), secEdu::setSchoolName);
        updateIfNotNull(userWrapper.getSecondaryPassingPercentage(), secEdu::setPassingPercentage);
        updateIfNotNull(userWrapper.getSecondaryBoardName(), secEdu::setBoardName);
        updateIfNotNull(userWrapper.getSecondaryPassingYear(), secEdu::setPassingYear);

        HigherSecondaryEducation highSecEdu = education.getHigherSecondaryEducation();
        updateIfNotNull(userWrapper.getHigherSecondarySchoolName(), highSecEdu::setSchoolName);
        updateIfNotNull(userWrapper.getHigherSecondaryPassingPercentage(), highSecEdu::setPassingPercentage);
        updateIfNotNull(userWrapper.getHigherSecondaryBoardName(), highSecEdu::setBoardName);
        updateIfNotNull(userWrapper.getHigherSecondaryStream(), highSecEdu::setStream);
        updateIfNotNull(userWrapper.getHigherSecondaryPassingYear(), highSecEdu::setPassingYear);

        DiplomaEducation dipEdu = education.getDiplomaEducation();
        updateIfNotNull(userWrapper.getDiplomaCollegeName(), dipEdu::setCollegeName);
        updateIfNotNull(userWrapper.getDiplomaCourseName(), dipEdu::setCourseName);
        updateIfNotNull(userWrapper.getDiplomaStartYear(), dipEdu::setStartYear);
        updateIfNotNull(userWrapper.getDiplomaPassingYear(), dipEdu::setPassingYear);
        updateIfNotNull(userWrapper.getDiplomaPassingPercentage(), dipEdu::setPassingPercentage);

        BachelorsEducation bachEdu = education.getBachelorsEducation();
        updateIfNotNull(userWrapper.getBachelorsCollegeName(), bachEdu::setCollegeName);
        updateIfNotNull(userWrapper.getBachelorsCourseName(), bachEdu::setCourseName);
        updateIfNotNull(userWrapper.getBachelorsStartYear(), bachEdu::setStartYear);
        updateIfNotNull(userWrapper.getBachelorsPassingYear(), bachEdu::setPassingYear);
        updateIfNotNull(userWrapper.getBachelorsPassingPercentage(), bachEdu::setPassingPercentage);

        MastersEducation mastEdu = education.getMastersEducation();
        updateIfNotNull(userWrapper.getMastersCollegeName(), mastEdu::setCollegeName);
        updateIfNotNull(userWrapper.getMastersCourseName(), mastEdu::setCourseName);
        updateIfNotNull(userWrapper.getMastersStartYear(), mastEdu::setStartYear);
        updateIfNotNull(userWrapper.getMastersPassingYear(), mastEdu::setPassingYear);
        updateIfNotNull(userWrapper.getMastersPassingPercentage(), mastEdu::setPassingPercentage);
    }

    // Helper method to update only if the value is not null
    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }


    @Override
    @Transactional
    public StudentWrapper updateProfilePicture(UserDetails userDetails, MultipartFile file) {
        return new StudentWrapper(updateProfileImage(userDetails, file));

    }


    private File getFile(MultipartFile file, Users user) {
        String userFolderPath = String.format("%s/%s/profile/profile-picture", rootFolderPath, user.getUserEmail());

        File profileFolder = new File(userFolderPath);
        if (!profileFolder.exists()) {
            profileFolder.mkdirs();
        }

        // Extract file extension
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // Define new file name
        String newFilename = "profile-pic" + fileExtension;
        return new File(profileFolder, newFilename);
    }

    @Override
    public StudentWrapper uploadResume(UserDetails userDetails, MultipartFile file) {
        Users user = findByUserEmail(userDetails.getUsername());
        String oldFilePath = user.getResumeFilePath(); // Get the old file path
        // Check if the old file exists and delete it
        getOldFileAndDelete(oldFilePath);
        File resumeFile = getResumeFile(file, user);
        try {
            file.transferTo(resumeFile);
        } catch (IOException e) {
            throw new RuntimeException("Error saving profile picture: " + e.getMessage(), e);
        }

        String relativePath = resumeFile.getAbsolutePath().replaceFirst("^" + rootFolderPath, "/storage/Internship-Portal");
        user.setResumeFilePath(relativePath);
        userRepository.save(user);
        return new StudentWrapper(user);
    }

    @Override
    public Resource downloadResume(String filePath) throws IOException {
        return new InputStreamResource(new FileInputStream(new File(filePath)));
    }

    @Override
    public StudentWrapper findStudentByStudentId(Long studentId) {
        Users user = userRepository.findByUserId(studentId);
        return new StudentWrapper(user);
    }

    @Override
    public InstructorWrapper getProfileDetails(UserDetails userDetails) {
        Users user = userRepository.findByUserEmail(userDetails.getUsername());
        return new InstructorWrapper(user);
    }

    @Override
    public InstructorWrapper updateProfilePictureOfInstructors(UserDetails userDetails, MultipartFile file) {
        return new InstructorWrapper(updateProfileImage(userDetails, file));
    }

    @Override
    public InstructorWrapper updateInstructor(UserDetails userDetails, InstructorWrapper instructorWrapper) {
        log.info("Update Instructor");
        Users instructor = userRepository.findByUserEmail(userDetails.getUsername());
        updateIfNotNull(instructorWrapper.getUserName(), instructor::setUserName);
        updateIfNotNull(instructorWrapper.getPhoneNumber(), instructor::setUserPhoneNumber);
        return new InstructorWrapper(userRepository.save(instructor));

    }

    @Override
    public InstructorWrapper addInstructor(InstructorWrapper instructor) {

        Users user = new Users();
        user.setUserName(instructor.getUserName());
        user.setUserEmail(instructor.getUserEmail());
        user.setUserPhoneNumber(instructor.getPhoneNumber());
        user.setUserPassword(passwordEncoder.encode("Test@123"));
        user.addRole("ROLE_INSTRUCTOR");
        DepartmentDetails department = departmentRepository.findByDepartmentName(instructor.getDepartment()).orElseThrow(() -> new RuntimeException("Please select a valid department"));
        user.setDepartment(department);
        try {
            return new InstructorWrapper(usersRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new UserCreationException("Error adding Instructor: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unable to add Instructor");

        }
    }


    private void getOldFileAndDelete(String oldFilePath) {
        if (oldFilePath != null && !oldFilePath.isEmpty()) {
            String oldAbsolutePath = rootFolderPath + oldFilePath.replaceFirst("^/storage/Internship-Portal", "");
            File oldFile = new File(oldAbsolutePath);
            if (oldFile.exists() && oldFile.isFile()) {
                boolean deleted = oldFile.delete();
                if (!deleted) {
                    throw new RuntimeException("Failed to delete old profile picture");
                }
            }
        }
    }

    private File getResumeFile(MultipartFile file, Users user) {
        String userFolderPath = String.format("%s/%s/profile/resume", rootFolderPath, user.getUserEmail());
        File resumeFolder = new File(userFolderPath);
        if (!resumeFolder.exists()) {
            resumeFolder.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = user.getUserEmail() + "_resume" + fileExtension;
        return new File(resumeFolder, newFilename);
    }

    private Users updateProfileImage(UserDetails userDetails, MultipartFile file) {
        Users user = findByUserEmail(userDetails.getUsername());
        String oldFilePath = user.getProfilePhotoFilePath(); // Get the old file path

        // Check if the old file exists and delete it
        getOldFileAndDelete(oldFilePath);

        File profilePicFile = getFile(file, user);

        try {
            file.transferTo(profilePicFile);
        } catch (IOException e) {
            throw new RuntimeException("Error saving profile picture: " + e.getMessage(), e);
        }

        // Convert absolute path to a relative path for database storage
        String relativePath = profilePicFile.getAbsolutePath().replaceFirst("^" + rootFolderPath, "/storage/Internship-Portal");
        user.setProfilePhotoFilePath(relativePath);
        return userRepository.save(user);
    }

}
