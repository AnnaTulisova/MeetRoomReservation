package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.Meetroom;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.repos.UserRepository;
import org.example.meetroomreservation.service.MeetroomService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class CalendarController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MeetroomService meetroomService;

    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal User user, Map<String, Object> model){
        boolean onlyNames = true;
        List<Meetroom> meetRooms = meetroomService.findAll(onlyNames);
        model.put("meetrooms", meetRooms);

        /*List<User> users = userService.findAll();
        model.put("users", users);*/
        return "calendar";
    }
}
