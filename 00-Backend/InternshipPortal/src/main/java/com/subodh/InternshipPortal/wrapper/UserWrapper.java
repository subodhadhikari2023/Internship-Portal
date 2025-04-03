package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Users;
import lombok.Data;

@Data
public class UserWrapper {
    private String userEmail;
    private String userName;
    private Long phoneNumber;
    public UserWrapper(Users user) {
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.phoneNumber = user.getUserPhoneNumber();
    }
}
