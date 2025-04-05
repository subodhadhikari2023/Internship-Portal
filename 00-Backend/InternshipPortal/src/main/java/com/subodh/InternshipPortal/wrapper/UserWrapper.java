package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Users;
import lombok.Data;

/**
 * The type User wrapper.
 */
@Data
public class UserWrapper {
    private String userEmail;
    private String userName;
    private Long phoneNumber;

    /**
     * Instantiates a new User wrapper.
     *
     * @param user the user
     */
    public UserWrapper(Users user) {
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.phoneNumber = user.getUserPhoneNumber();
    }
}
