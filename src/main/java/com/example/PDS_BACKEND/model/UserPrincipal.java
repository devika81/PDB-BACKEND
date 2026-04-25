package com.example.PDS_BACKEND.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// This class converts your Users entity into a format Spring Security understands
// It implements UserDetails (required by Spring Security)
public class UserPrincipal implements UserDetails {

    // Holds your actual user data from DB
    private Users users;

    // Constructor to receive Users object
    public UserPrincipal(Users users) {
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Defines the role/permission of the user
        // Here, every user is given ADMIN role
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public String getPassword() {
        // Returns password from DB
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        // Returns username from DB
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Returns true → account is not expired
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        // Returns true → account is not locked
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Returns true → password is still valid
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        // Returns true → user is active
        return UserDetails.super.isEnabled();
    }
}