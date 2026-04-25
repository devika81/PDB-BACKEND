package com.example.PDS_BACKEND.controlller;

import com.example.PDS_BACKEND.model.Users;
import com.example.PDS_BACKEND.service.JWTService;
import com.example.PDS_BACKEND.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    //method for registering a new user
    @PostMapping("/registerUser")
    public Users registerUser(@RequestBody Users users)   //--> 'users' is having new user's data
    {
        return userService.saveUserToDB(users);          //calling method 'saveUserToDB' in UserService class(here also, we are passing new user's data through 'users'

    }

    //method for user login
    @PostMapping("/loginUser")
    public String loginUser(@RequestBody Users users)
    {
        //authenticates user entered username and password while logging
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));

        //message shown if login is success
        if(authentication.isAuthenticated())
        {
            //return "LOGIN SUCCESS!";
            return jwtService.generateToken(users.getUsername());
        }
        //message shown if login is failed
        else
        {
            return "LOGIN FAILED";
        }
    }



}
