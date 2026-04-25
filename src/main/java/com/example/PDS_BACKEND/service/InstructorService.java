package com.example.PDS_BACKEND.service;

import com.example.PDS_BACKEND.dao.InstructorRepo;
import com.example.PDS_BACKEND.model.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {

    @Autowired
    InstructorRepo instructorRepo;

    public void addInstructorToDB(Instructor instructor) {

        instructorRepo.save(instructor);

    }

    public List<Instructor> getAllInstructorsFromDB() {
        return instructorRepo.findAll();
    }
}
