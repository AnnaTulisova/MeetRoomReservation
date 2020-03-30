package org.example.meetroomreservation.service;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.domain.ReservationViewModel;

import java.util.List;

public interface ReservationService {
    List<Reservation> findAll();
    List<Reservation> findByDatetimeAndMeetroomId(String datetime, Integer meetroom_id);
    ReservationViewModel findReservationsWithUsers(String datetime, Integer meetroom_id);
    List<Reservation> findAllByOrderByDatetimeAscMeetroomIdAsc();
    List<Reservation> findTotalReservations();
    List<ReservationViewModel> findReservationsWithUsers();
    void saveChanges(ReservationViewModel reservationViewModel, String datetime, Integer meetroom_id);
    void deleteOldUsers(List<Integer> user_ids, String dateTime);
    void save(Reservation reservation);
    void addNewUsers(List<Integer> user_ids, ReservationViewModel reservationViewModel);
    void deleteByDatetimeAndMeetroomId(String datetime, Integer meetroom_id);

}
