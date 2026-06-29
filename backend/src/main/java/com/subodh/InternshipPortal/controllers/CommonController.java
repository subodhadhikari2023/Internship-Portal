package com.subodh.InternshipPortal.controllers;

import com.subodh.InternshipPortal.services.UserService;
import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.Response;
import com.subodh.InternshipPortal.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * The type Common controller.
 */
@Slf4j
@RestController
@RequestMapping("api/v1/common")
@CrossOrigin
public class CommonController {

    private final UserService userService;

    /**
     * Instantiates a new Common controller.
     *
     * @param userService the user service
     */
    public CommonController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Validates whether the supplied plain-text password matches the authenticated user's stored password.
     *
     * @param userDetails the currently authenticated user
     * @param apiRequest  the plain-text password to validate
     * @return 200 OK with {@code true} if the password matches, {@code false} otherwise
     */
    @PostMapping("validate-password")
    public ResponseEntity<?> validatePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody APIRequest<String> apiRequest) {
        return new ResponseEntity<>(new Response<>(userService.validatePassword(userDetails.getUsername(), apiRequest.getEntity())), HttpStatus.OK);
    }

    /**
     * Resets the authenticated user's password to the value supplied in the request body.
     *
     * @param userDetails the currently authenticated user
     * @param password    the new plain-text password wrapped in an {@link APIRequest}
     * @return 202 ACCEPTED with the updated {@link com.subodh.InternshipPortal.wrapper.UserWrapper},
     *         or 406 NOT ACCEPTABLE if the user cannot be found
     */
    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody APIRequest<String> password) {
        UserWrapper userWrapper = userService.resetPasswordUserFoundByEmail(userDetails.getUsername(), password.getEntity());
        if (userWrapper != null) {
            return new ResponseEntity<>(new Response<>(userWrapper), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

    }


}
