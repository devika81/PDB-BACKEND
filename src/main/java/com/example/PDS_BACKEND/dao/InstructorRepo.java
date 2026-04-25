package com.example.PDS_BACKEND.dao;

import com.example.PDS_BACKEND.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Integer> {
    Optional<Instructor> findByInstructorName(String instructor_name);

}
