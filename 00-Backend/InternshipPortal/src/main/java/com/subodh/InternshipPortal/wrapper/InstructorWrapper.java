package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Users;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
public class InstructorWrapper {
    public InstructorWrapper(Users user) {
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.phoneNumber = user.getUserPhoneNumber();
        this.profilePictureFilePath = user.getProfilePhotoFilePath();
        this.department = user.getDepartment().getDepartmentName();
        this.profileCompleteness = calculateProfileCompleteness();
        this.role=user.getRoles().get(0).getRoleName();


    }

    private String userEmail;
    private String userName;
    private Long phoneNumber;
    private String department;
    private String profilePictureFilePath;
    private double profileCompleteness;
    private String role;


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
