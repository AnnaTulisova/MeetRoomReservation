package org.example.meetroomreservation.service;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.domain.ReservationViewModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReservationService {
    ReservationViewModel findReservationsWithUsers(String datetime, Integer meetroom_id);

    List<Reservation> findAll();
    List<Reservation> findByDatetimeAndMeetroomId(String datetime, Integer meetroom_id);

    List<Reservation> findAllByOrderByDatetimeAscMeetroomIdAsc();
    List<Reservation> findTotalReservations();
    List<ReservationViewModel> findReservationsWithUsers();

    void addReservation(String userId, Integer meetroomId, String datetime, String duration);
    void editReservation(String datetime, Integer meetroomId, String newDatetime,
                        Integer newMeetroomId, String userIds, String duration);
    void saveChanges(ReservationViewModel reservationViewModel, String datetime, Integer meetroom_id);
    void deleteOldUsers(List<Integer> user_ids, String dateTime);
    void save(Reservation reservation);
    void addNewUsers(List<Integer> user_ids, ReservationViewModel reservationViewModel);
    void deleteByDatetimeAndMeetroomId(String datetime, Integer meetroom_id);

}
