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
    public String calendar(@AuthenticationPrincipal User user, Map<String, Object> model){
        List<ReservationViewModel> reservations = reservationService.findReservationsWithUsers();
        model.put("reservations", reservations);
        return "calendar";
    }

    @GetMapping("/calendar/reservation")
    public String reservationInfo(@RequestParam String datetime, @RequestParam Integer meetroom_id, Map<String, Object> model){
        ReservationViewModel reservation = reservationService.findReservationsWithUsers(datetime, meetroom_id);
        model.put("reservation", reservation);
        model.put("meetroom_id", meetroom_id);
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
    public String delete(@RequestParam String datetime, @RequestParam Integer meetroom_id,Map<String,Object> model){
        reservationService.deleteByDatetimeAndMeetroomId(datetime, meetroom_id);
        return "redirect:/calendar";
    }

    @GetMapping("/calendar/reservation/edit")
    public String edit(@RequestParam String datetime, @RequestParam Integer meetroom_id,Map<String, Object> model){
        ReservationViewModel reservationViewModel = reservationService.findReservationsWithUsers(datetime, meetroom_id);
        String user_ids = userService.getUserIdsFromReservation(reservationViewModel);
        model.put("reservation", reservationViewModel);
        model.put("user_ids", user_ids);
        model.put("meetroom_id", reservationViewModel.getMeetroom().getId().toString());
        return "edit";
    }
    @PostMapping("/calendar/reservation/edit")
    private String edit(@RequestParam String datetime, @RequestParam Integer meetroom_id,
                        @RequestParam String newdatetime, @RequestParam Integer newmeetroom_id,
                        @RequestParam String user_ids, @RequestParam String duration, Map<String, Object> model){
        ReservationViewModel oldReservation = reservationService.findReservationsWithUsers(datetime, meetroom_id);
        String oldUserIds = userService.getUserIdsFromReservation(oldReservation);
        int[] old_ids_array = userService.updateStringIdsToIntArray(oldUserIds);
        int[] new_ids_array = userService.updateStringIdsToIntArray(user_ids);

        List<Integer> usersIdsToDelete = userService.findDifferentUsers(old_ids_array, new_ids_array);
        reservationService.deleteOldUsers(usersIdsToDelete, datetime);

        if(!oldReservation.getDatetime().toString().equals(newdatetime)) oldReservation.setDatetime(LocalDateTime.parse(newdatetime));
        if(!oldReservation.getDuration().toString().equals(duration)) oldReservation.setDuration(LocalTime.parse(duration));
        if(!oldReservation.getMeetroom().getId().equals(newmeetroom_id)) oldReservation.setMeetroom(meetroomService.findById(newmeetroom_id));

        reservationService.saveChanges(oldReservation, datetime, meetroom_id);

        List<Integer> newUserIds = userService.findDifferentUsers(new_ids_array, old_ids_array);
        reservationService.addNewUsers(newUserIds, oldReservation);

        return "redirect:/calendar/reservation?meetroom_id="+meetroom_id+"&datetime="+newdatetime;
    }
}
