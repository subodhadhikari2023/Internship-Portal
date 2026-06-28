package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminWrapper {
    private String userName;
    private String email;
    private String role;
    private Long phoneNumber;
    private String profilePictureFilePath;

    public AdminWrapper(Users user) {
        this.email=user.getUserEmail();
        this.userName=user.getUserName();
        this.role=user.getRoles().get(0).getRoleName();
        this.phoneNumber=user.getUserPhoneNumber();
        this.profilePictureFilePath=user.getProfilePhotoFilePath();
    }
}
