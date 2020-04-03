package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.ReservationViewModel;
import org.example.meetroomreservation.service.ReservationService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
public class CalendarController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @GetMapping("/calendar")
    public String calendar(Map<String, Object> model) {
        List<ReservationViewModel> reservations = reservationService.findReservationsWithUsers();

        model.put("reservations", reservations);
        return "calendar";
    }

    @GetMapping("/calendar/reservation")
    public String reservationInfo(@RequestParam String datetime, @RequestParam Integer meetroomId, Map<String, Object> model) {
        ReservationViewModel reservation = reservationService.findReservationsWithUsers(datetime, meetroomId);

        model.put("reservation", reservation);
        model.put("meetroomId", meetroomId);
        model.put("datetime", datetime);
        return "reservationInfo";
    }

    @GetMapping("/add")
    public String add(Map<String, Object> model){
        return "add";
    }

    @PostMapping("/add")
    public String add(@RequestParam String userId, @RequestParam Integer meetroomId, @RequestParam String datetime,
                                 @RequestParam String duration) {
        reservationService.addReservation(userId,  meetroomId, datetime, duration);
        return "redirect:/calendar";
    }

    @PostMapping("/calendar/reservation/delete")
    public String delete(@RequestParam String datetime, @RequestParam Integer meetroomId) {
        reservationService.deleteByDatetimeAndMeetroomId(datetime, meetroomId);
        return "redirect:/calendar";
    }

    @GetMapping("/calendar/reservation/edit")
    public String edit(@RequestParam String datetime, @RequestParam Integer meetroomId,Map<String, Object> model) {
        ReservationViewModel reservationViewModel = reservationService.findReservationsWithUsers(datetime, meetroomId);
        String userIds = userService.getUserIdsFromReservation(reservationViewModel);
        model.put("reservation", reservationViewModel);
        model.put("userIds", userIds);
        model.put("meetroomId", reservationViewModel.getMeetroom().getId().toString());
        return "edit";
    }

    @PostMapping("/calendar/reservation/edit")
    private String edit(@RequestParam String datetime, @RequestParam Integer meetroomId, @RequestParam String newDatetime,
                        @RequestParam Integer newMeetroomId, @RequestParam String userIds, @RequestParam String duration) {
        reservationService.editReservation(datetime, meetroomId, newDatetime, newMeetroomId, userIds, duration);
        return "redirect:/calendar/reservation?meetroomId="+newMeetroomId+"&datetime="+newDatetime;
    }
}
