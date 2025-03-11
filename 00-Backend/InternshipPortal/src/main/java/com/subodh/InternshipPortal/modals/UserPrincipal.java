package com.subodh.InternshipPortal.modals;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * The type User principal.
 */
public class UserPrincipal implements UserDetails {
    private Users user;

    /**
     * Instantiates a new User principal.
     *
     * @param user the user
     */
    public UserPrincipal(Users user) {
        this.user = user;

    }

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user
                .getRoles()
                .stream()
                .map(
                        roles -> new SimpleGrantedAuthority(
                                roles
                                        .getAuthority()))
                .toList();
    }

    /**
     * @return
     */
    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    /**
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
