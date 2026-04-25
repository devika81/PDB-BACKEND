package com.example.PDS_BACKEND.controlller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String greet(HttpServletRequest request)
    {
        return "hello  " + request.getSession().getId();
    }

}
