package com.example.PDS_BACKEND.service;

import com.example.PDS_BACKEND.dao.InstructorRepo;
import com.example.PDS_BACKEND.model.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public void updateInstructorInDB(String instructorName, Map<String, Object> updates) {

        Instructor instructor = instructorRepo.findByInstructorName(instructorName)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Loop through updates and set values dynamically
        updates.forEach((key, value) -> {

            switch (key) {
                case "instructor_id":
                    instructor.setInstructor_id((int) value);
                    break;

                case "instructorName":
                    instructor.setInstructorName((String) value);
                    break;

                case "years_of_experience":
                    instructor.setYears_of_experience((int) value);
                    break;

                case "location":
                    instructor.setLocation((String) value);
                    break;

                case "email":
                    instructor.setEmail((String) value);
                    break;

                case "phone_number":
                    instructor.setPhone_number((String) value);
                    break;

                default:
                    throw new RuntimeException("Invalid field: " + key);
            }
        });

        instructorRepo.save(instructor);
    }

    public void deleteInstructorFromDB(String instructorName) {
        Instructor instructor = instructorRepo.findByInstructorName(instructorName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        instructorRepo.delete(instructor);
    }
}
