package com.example.PDS_BACKEND.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injects (automatically provides) an implementation of UserDetailsService
    // Spring will find your MyUserDetailsService (@Service) and inject it here
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JWTFilter jwtFilter;

    //for user login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authProvider()
    {
        // Creates an object that handles authentication logic (login verification)
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();

        // Tells the authentication provider:
        // "Use this service to load user details from DB"
        // Internally, it will call loadUserByUsername()
        daoAuthProvider.setUserDetailsService(userDetailsService);

        // Sets password encoder
        // NoOpPasswordEncoder means passwords are stored as plain text (NOT secure)
        // Used only for learning/testing
        //daoAuthProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        //Implementing BCrypt password encoder
        //The BCryptPasswordEncoder takes the plain text password from the login attempt and applies the same hashing algorithm done while registration
        //It then compares this hash with the stored hash from the database
        //If they match, authentication succeeds
                               //[OR]
        // Implementing BCrypt password encoder
        // During login, the entered plain password is verified against the stored hashed password.
        // BCrypt extracts the salt from the stored hash, re-processes the entered password using it,
        // and checks if both match using the matches() method.
        // If they match, authentication succeeds.
        daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        // Returns the configured authentication provider
        // Because of @Bean, Spring will register this and use it for authentication
        return daoAuthProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disables CSRF protection
                .csrf(customizer -> customizer.disable())


                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/registerUser","/api/loginUser").permitAll()  // '/registerUser' request is permitted without providing authentication details
                        .anyRequest().authenticated())   //Requires authentication for ALL incoming HTTP requests

                //implementing Oauth2
                //.oauth2Login(Customizer.withDefaults())

                // Enables HTTP Basic Authentication (username & password via headers)
                .httpBasic(Customizer.withDefaults())

                // Configures session management
                // Makes the application STATELESS (no session will be created or stored)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //to validate the token generated in the server end, we are creating a JWTFilter
                //which should work before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Builds and returns the configured security filter chain
        return http.build();
    }


}
