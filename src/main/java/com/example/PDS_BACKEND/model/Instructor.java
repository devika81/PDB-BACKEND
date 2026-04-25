package com.example.PDS_BACKEND.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instructor_id;
    @Column(name = "instructor_name")
    private String instructorName;
    private int years_of_experience;
    private String location;
    private String email;
    private String phone_number;

}
