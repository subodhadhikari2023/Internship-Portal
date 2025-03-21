package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Users;
import lombok.Data;

@Data
public class UserWrapper {

    String userEmail;

    public UserWrapper(Users user) {
        this.userEmail = user.getUserEmail();
    }
}
