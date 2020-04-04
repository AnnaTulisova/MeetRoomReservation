package org.example.meetroomreservation.controller;

import org.example.meetroomreservation.domain.viewModels.ReservationView;
import org.example.meetroomreservation.domain.requestModels.*;
import org.example.meetroomreservation.service.ReservationService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CalendarController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET, produces = "application/json")
    public List<ReservationView> calendar() {
        List<ReservationView> reservations = reservationService.findReservationsWithUsers();
        return reservations;
    }

    @RequestMapping(value = "/calendar/reservation", method = RequestMethod.GET, produces = "application/json")
    public ReservationView reservationInfo(@RequestBody ReservationRequest reservationRequest) {
        ReservationView reservation = reservationService.findReservationsWithUsers(
                reservationRequest.getDatetime(),
                reservationRequest.getMeetroomId());
        return reservation;
    }

    @PostMapping("/calendar/create")
    public ResponseEntity<String> add(@RequestBody ReservationAddEdit reservationAdd) {
        reservationService.addReservation(reservationAdd);
        return ResponseEntity.ok("Added");
    }

    @PostMapping("/calendar/reservation/delete")
    public ResponseEntity<String> delete(@RequestBody ReservationRequest reservationRequest) {
        reservationService.deleteByDatetimeAndMeetroomId(
                reservationRequest.getDatetime(), reservationRequest.getMeetroomId());
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/calendar/reservation/edit")
    public ResponseEntity<ReservationAddEdit> edit(@RequestBody ReservationRequest reservationRequest) {
        ReservationAddEdit reservationToEdit = reservationService.getReservationForEdit(reservationRequest);
        return ResponseEntity.ok(reservationToEdit);
    }

    @PutMapping("/calendar/reservation/edit")
    private ResponseEntity<Void> edit(@RequestBody ReservationEdit reservationEdit) {
        reservationService.editReservation(reservationEdit);
        return ResponseEntity.noContent().build();
    }
}
