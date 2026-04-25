package com.example.PDS_BACKEND.controlller;

import com.example.PDS_BACKEND.model.Instructor;
import com.example.PDS_BACKEND.service.InstructorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InstructorController {

    @Autowired
    InstructorService instructorService;

//Creating a list for instructors
//    List <Instructor> instructors = new ArrayList<>(List.of(
//            new Instructor(101,"Jim Primrose",35, "Scotland", "jim@primrosedrivingschool.com", "07394 133763"),
//            new Instructor(102,"Leighan Gordon",30, "Scotland", "leighan@primrosedrivingschool.com", "07764 486060")
//    ));


    @PostMapping("/addInstructors")
    public String addInstructors(@RequestBody Instructor instructor)
    {
        //instructors.add(instructor);
        instructorService.addInstructorToDB(instructor);
        return "Instructor successfully added!";
    }

    @GetMapping("/getInstructors")
    public List<Instructor>getInstructors()
    {
        //return instructors;
        return instructorService.getAllInstructorsFromDB();
    }

    @PostMapping("/updateInstructors")
    public void updateInstructors(@RequestBody Instructor instructor)
    {
        return;
    }

    @DeleteMapping("/deleteInstructors")
    public void deleteInstructors(@RequestBody Instructor instructor)
    {
        return;
    }




    @GetMapping("/getcsrftoken")
    public CsrfToken getCsrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute("_csrf");
    }


}
