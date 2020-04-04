package org.example.meetroomreservation.service;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.domain.requestModels.ReservationAddEdit;
import org.example.meetroomreservation.domain.requestModels.ReservationEdit;
import org.example.meetroomreservation.domain.requestModels.ReservationRequest;
import org.example.meetroomreservation.domain.viewModels.ReservationView;

import java.util.List;

public interface ReservationService {
    ReservationView findReservationsWithUsers(String datetime, Integer meetroom_id);

    List<Reservation> findAll();
    List<Reservation> findByDatetimeAndMeetroomId(String datetime, Integer meetroom_id);

    List<Reservation> findAllByOrderByDatetimeAscMeetroomIdAsc();
    List<Reservation> findTotalReservations();
    List<ReservationView> findReservationsWithUsers();

    ReservationAddEdit getReservationForEdit(ReservationRequest reservation);

    void addReservation(ReservationAddEdit reservationAddEdit);
    void editReservation(ReservationEdit reservationEdit);
    void saveChanges(ReservationView reservationView, String datetime, Integer meetroom_id);
    void deleteOldUsers(List<Integer> user_ids, String dateTime);
    void save(Reservation reservation);
    void addNewUsers(List<Integer> user_ids, ReservationView reservationView);
    void deleteByDatetimeAndMeetroomId(String datetime, Integer meetroom_id);

}
