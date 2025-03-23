package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Year;
import java.util.Set;

@Slf4j
@Data
public class StudentWrapper {
    // Personal Information
    private String userEmail;
    private String userName;
    private Long phoneNumber;
    private double profileCompleteness;

    // Secondary Education
    private String secondarySchoolName;
    private String secondaryBoardName;
    private Year secondaryPassingYear;
    private Double secondaryPassingPercentage;

    // Higher Secondary Education
    private String higherSecondarySchoolName;
    private String higherSecondaryBoardName;
    private Year higherSecondaryPassingYear;
    private Double higherSecondaryPassingPercentage;
    private String higherSecondaryStream;

    // Diploma Education
    private String diplomaCollegeName;
    private String diplomaCourseName;
    private Year diplomaStartYear;
    private Year diplomaPassingYear;
    private Double diplomaPassingPercentage;

    // Bachelors Education
    private String bachelorsCollegeName;
    private String bachelorsCourseName;
    private Year bachelorsStartYear;
    private Year bachelorsPassingYear;
    private Double bachelorsPassingPercentage;

    // Masters Education
    private String mastersCollegeName;
    private String mastersCourseName;
    private Year mastersStartYear;
    private Year mastersPassingYear;
    private Double mastersPassingPercentage;

    // Other Information
    private Set<String> skills;
    private String profilePictureFilePath;
    private String resumeFilePath;

    // Constructor to fetch user data
    public StudentWrapper(Users user) {
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.phoneNumber = user.getUserPhoneNumber();

        // Handling Education Fields
        if (user.getEducation() != null) {
            Education education = user.getEducation();

            if (education.getSecondaryEducation() != null) {
                SecondaryEducation secEdu = education.getSecondaryEducation();
                this.secondarySchoolName = secEdu.getSchoolName();
                this.secondaryBoardName = secEdu.getBoardName();
                this.secondaryPassingYear = secEdu.getPassingYear();
                this.secondaryPassingPercentage = secEdu.getPassingPercentage();
            }

            if (education.getHigherSecondaryEducation() != null) {
                HigherSecondaryEducation hsEdu = education.getHigherSecondaryEducation();
                this.higherSecondarySchoolName = hsEdu.getSchoolName();
                this.higherSecondaryBoardName = hsEdu.getBoardName();
                this.higherSecondaryPassingYear = hsEdu.getPassingYear();
                this.higherSecondaryPassingPercentage = hsEdu.getPassingPercentage();
                this.higherSecondaryStream = hsEdu.getStream();
            }

            if (education.getDiplomaEducation() != null) {
                DiplomaEducation dipEdu = education.getDiplomaEducation();
                this.diplomaCollegeName = dipEdu.getCollegeName();
                this.diplomaCourseName = dipEdu.getCourseName();
                this.diplomaStartYear = dipEdu.getStartYear();
                this.diplomaPassingYear = dipEdu.getPassingYear();
                this.diplomaPassingPercentage = dipEdu.getPassingPercentage();
            }

            if (education.getBachelorsEducation() != null) {
                BachelorsEducation bachEdu = education.getBachelorsEducation();
                this.bachelorsCollegeName = bachEdu.getCollegeName();
                this.bachelorsCourseName = bachEdu.getCourseName();
                this.bachelorsStartYear = bachEdu.getStartYear();
                this.bachelorsPassingYear = bachEdu.getPassingYear();
                this.bachelorsPassingPercentage = bachEdu.getPassingPercentage();
            }

            if (education.getMastersEducation() != null) {
                MastersEducation mastEdu = education.getMastersEducation();
                this.mastersCollegeName = mastEdu.getCollegeName();
                this.mastersCourseName = mastEdu.getCourseName();
                this.mastersStartYear = mastEdu.getStartYear();
                this.mastersPassingYear = mastEdu.getPassingYear();
                this.mastersPassingPercentage = mastEdu.getPassingPercentage();
            }

            this.skills = user.getStudentSkills();
            this.resumeFilePath = user.getResumeFilePath();
            this.profilePictureFilePath = user.getProfilePhotoFilePath();
        }

        profileCompleteness = this.calculateProfileCompleteness();
    }

    // Calculate profile completeness based on filled fields
    public double calculateProfileCompleteness() {
        int totalFields = 0;
        int filledFields = 0;

        try {
            for (var field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                totalFields++;
                if (field.get(this) != null) {
                    filledFields++;
                }
            }
        } catch (IllegalAccessException e) {
            log.warn(e.getMessage());
        }

        return ((double) filledFields / totalFields) * 100;
    }
}
