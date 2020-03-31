package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.domain.ReservationViewModel;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.service.MeetroomService;
import org.example.meetroomreservation.service.ReservationService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
public class CalendarController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetroomService meetroomService;

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
                                 @RequestParam String duration, Map<String, Object> model) throws ParseException {
        if (userId.contains(";")) {
            int[] userIds = userService.updateStringIdsToIntArray(userId);
            for(Integer uId : userIds){
                Reservation reservation = new Reservation(LocalDateTime.parse(datetime), LocalTime.parse(duration),
                                            userService.findById(uId),  meetroomService.findById(meetroomId));
                reservationService.save(reservation);
            }
        } else {
            Reservation reservation = new Reservation(LocalDateTime.parse(datetime),LocalTime.parse(duration),
                                        userService.findById(Integer.parseInt(userId)), meetroomService.findById(meetroomId));
            reservationService.save(reservation);
        }
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
    private String edit(@RequestParam String datetime, @RequestParam Integer meetroomId,
                        @RequestParam String newDatetime, @RequestParam Integer newMeetroomId,
                        @RequestParam String userIds, @RequestParam String duration) {
        //find old records about reservation
        ReservationViewModel oldReservation = reservationService.findReservationsWithUsers(datetime, meetroomId);
        //find last users for the reservation
        String oldUserIds = userService.getUserIdsFromReservation(oldReservation);
        //get ids array from last users ids
        int[] oldIdsArray = userService.updateStringIdsToIntArray(oldUserIds);
        //get ids array from new users
        int[] newIdsArray = userService.updateStringIdsToIntArray(userIds);
        //find not non-current users for the reservation
        List<Integer> usersIdsToDelete = userService.findDifferentUsers(oldIdsArray, newIdsArray);
        //delete entries with their ids in case of any
        if(usersIdsToDelete.size() > 0){
            reservationService.deleteOldUsers(usersIdsToDelete, datetime);
        }
        //check for update the reservation fields
        //if they were updated refresh them in the reservation
        if(!oldReservation.getDatetime().toString().equals(newDatetime)) {
            oldReservation.setDatetime(LocalDateTime.parse(newDatetime));
        }
        if(!oldReservation.getDuration().toString().equals(duration)) {
            oldReservation.setDuration(LocalTime.parse(duration));
        }
        if(!oldReservation.getMeetroom().getId().equals(newMeetroomId)) {
            oldReservation.setMeetroom(meetroomService.findById(newMeetroomId));
        }
        //save current reservation in DB
        reservationService.saveChanges(oldReservation, datetime, meetroomId);
        //check for the new users for the reservation
        List<Integer> usersIdsToAdd = userService.findDifferentUsers(newIdsArray, oldIdsArray);
        //add the users for the reservation in case of any
        if(usersIdsToAdd.size() > 0){
            reservationService.addNewUsers(usersIdsToAdd, oldReservation);
        }
        return "redirect:/calendar/reservation?meetroomId="+newMeetroomId+"&datetime="+newDatetime;
    }
}
