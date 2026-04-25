package com.example.PDS_BACKEND.service;

import com.example.PDS_BACKEND.dao.UserRepo;
import com.example.PDS_BACKEND.model.UserPrincipal;
import com.example.PDS_BACKEND.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Marks this class as a Spring Service component
// Spring will automatically detect it and create an object (bean) for it
@Service
public class MyUserDetailsService implements UserDetailsService {

    // Injects UserRepo (your database access layer)
    // Spring will automatically provide an implementation at runtime
    @Autowired
    UserRepo repo;

    // This method is called by Spring Security during login
    // It must return user details based on the username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Fetch user from database using username
        Users users = repo.findByUsername(username);

        // If user not found in DB
        if(users == null)
        {
            // Just a console message (for debugging)
            System.out.println("User 404");

            // Throw exception → Spring Security will handle this as "login failed"
            throw new UsernameNotFoundException("User 404");
        }

        // If user is found:
        // we wrap it in a UserPrincipal object and return it
        return new UserPrincipal(users);
    }
}
