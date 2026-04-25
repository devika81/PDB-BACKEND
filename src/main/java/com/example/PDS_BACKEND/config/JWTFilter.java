package com.example.PDS_BACKEND.config;

import com.example.PDS_BACKEND.service.JWTService;
import com.example.PDS_BACKEND.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Marks this class as a Spring Bean so it gets managed automatically
@Component
// Ensures this filter runs once per request
public class JWTFilter extends OncePerRequestFilter {


    // Injects JWTService to handle token operations (extract, validate)
    @Autowired
    JWTService jwtService;

    // Used to get beans manually from Spring container (like MyUserDetailsService)
    @Autowired
    ApplicationContext context;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // Get the Authorization header from incoming request
        String authHeader = request.getHeader("Authorization");

        // Will store JWT token
        String token = null;

        // Will store extracted username from token
        String userName = null;

        // Check if header exists and starts with "Bearer "
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            // Extract token (remove "Bearer " prefix)
            token = authHeader.substring(7);

            // Extract username from token using JWTService
            userName = jwtService.extractUserName(token);
        }

        // If username is extracted AND no authentication is already set in context
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            // Load user details from database using username
            UserDetails userDetails = context
                    .getBean(MyUserDetailsService.class)
                    .loadUserByUsername(userName);

            // Validate token (check signature, expiry, etc.)
            if(jwtService.validateToken(token, userDetails))
            {
                // Create authentication object with user details and authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach request details (IP, session, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set authentication in SecurityContext (marks user as logged in)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}