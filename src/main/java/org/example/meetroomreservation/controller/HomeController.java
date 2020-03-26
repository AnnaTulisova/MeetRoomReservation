package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping
    public  String home(Map<String, Object> model){
        List<User> users = userService.findAll();
        model.put("users", users);
        return "home";
    }
}
