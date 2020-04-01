package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.service.ReservationService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    public List<User> home() {
        return  userService.findAll();
    }

    @GetMapping
    public  String home(Map<String, Object> model){
        List<User> users = userService.findAll();
        model.put("users", users);
        return "home";
    }

}
