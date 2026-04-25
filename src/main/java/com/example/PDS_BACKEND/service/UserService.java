package com.example.PDS_BACKEND.service;

import com.example.PDS_BACKEND.dao.UserRepo;
import com.example.PDS_BACKEND.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo repo;

    //implementing bcrypt encoder for hashing the password entered by the user while registering
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);  //12-->specifies no. of iterations of hashing(2^12)

    public Users saveUserToDB(Users users)
    {
        //encoding/hashing the plain password entered by the user while registering
        users.setPassword(encoder.encode(users.getPassword()));
        //Just a console message (for debugging)
        System.out.println(users.getPassword());
        //saving the new hashed password into db
        return repo.save(users);
    }

}
