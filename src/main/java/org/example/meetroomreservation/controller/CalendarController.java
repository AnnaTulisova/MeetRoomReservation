package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.Meetroom;
import org.example.meetroomreservation.repos.MeetroomRepository;
import org.example.meetroomreservation.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class CalendarController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeetroomRepository meetroomRepository;

    @GetMapping("/calendar")
    public String calendar(Map<String, Object> model){

        if(meetroomRepository != null){
            Iterable<Meetroom> meetRooms = meetroomRepository.findAllByOrderByIdAsc();
            model.put("meetrooms", meetRooms);
        }else{
           model.put("message", "none");
        }
        return "calendar";

    }
}
