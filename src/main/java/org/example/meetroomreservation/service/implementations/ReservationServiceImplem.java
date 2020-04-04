package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.*;
import org.example.meetroomreservation.domain.requestModels.ReservationAddEdit;
import org.example.meetroomreservation.domain.requestModels.ReservationEdit;
import org.example.meetroomreservation.domain.requestModels.ReservationRequest;
import org.example.meetroomreservation.domain.viewModels.MeetroomView;
import org.example.meetroomreservation.domain.viewModels.ReservationView;
import org.example.meetroomreservation.domain.viewModels.UserView;
import org.example.meetroomreservation.repos.ReservationRepository;
import org.example.meetroomreservation.service.MeetroomService;
import org.example.meetroomreservation.service.ReservationService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImplem implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MeetroomService meetroomService;

    @Autowired
    private UserService userService;

    public List<Reservation> findAll() { return reservationRepository.findAll();}

    public void save(Reservation reservation){
        reservationRepository.save(reservation);
    }

    public List<Reservation> findByDatetimeAndMeetroomId(String datetime, Integer meetroomId) {
        return reservationRepository.findByDatetimeAndMeetroomId(LocalDateTime.parse(datetime), meetroomId);
    }

    public  void addReservation(ReservationAddEdit reservationToAdd) {
        if (reservationToAdd.getUserIds().contains(";")) {
            int[] userIds = userService.updateStringIdsToIntArray(reservationToAdd.getUserIds());
            for(Integer uId : userIds){
                Reservation reservation = new Reservation(
                        LocalDateTime.parse(reservationToAdd.getDatetime()),
                        LocalTime.parse(reservationToAdd.getDuration()),
                        userService.findById(uId),
                        meetroomService.findById(reservationToAdd.getMeetroomId()));

                save(reservation);
            }
        } else {
            Reservation reservation = new Reservation(
                    LocalDateTime.parse(reservationToAdd.getDatetime()),
                    LocalTime.parse(reservationToAdd.getDatetime()),
                    userService.findById(Integer.parseInt(reservationToAdd.getUserIds())),
                    meetroomService.findById(reservationToAdd.getMeetroomId()));

            save(reservation);
        }

    }

    public void editReservation(ReservationEdit reservationEdit) {
        //find old records about reservation
        ReservationView oldReservation = findReservationsWithUsers(reservationEdit.getOldDatetime(),
                reservationEdit.getOldMeetroomId());
        //find last users for the reservation
        String oldUserIds = userService.getUserIdsFromReservation(oldReservation);
        //get ids array from last users ids
        int[] oldIdsArray = userService.updateStringIdsToIntArray(oldUserIds);
        //get ids array from new users
        int[] newIdsArray = userService.updateStringIdsToIntArray(reservationEdit.getUserIds());
        //find not non-current users for the reservation
        List<Integer> usersIdsToDelete = userService.findDifferentUsers(oldIdsArray, newIdsArray);
        //delete entries with their ids in case of any
        if(usersIdsToDelete.size() > 0){
            deleteOldUsers(usersIdsToDelete, reservationEdit.getOldDatetime());
        }
        //check for update the reservation fields
        //if they were updated refresh them in the reservation
        if(!oldReservation.getDatetime().toString().equals(reservationEdit.getDatetime())) {
            oldReservation.setDatetime(LocalDateTime.parse(reservationEdit.getDatetime()));
        }
        if(!oldReservation.getDuration().toString().equals(reservationEdit.getDuration())) {
            oldReservation.setDuration(LocalTime.parse(reservationEdit.getDuration()));
        }
        if(!oldReservation.getMeetroom().getId().equals(reservationEdit.getMeetroomId())) {
            Meetroom newMeetroom = meetroomService.findById(reservationEdit.getMeetroomId());
            oldReservation.setMeetroom(new MeetroomView(newMeetroom.getId(), newMeetroom.getName(), newMeetroom.getLocation()));
        }
        //save current reservation in DB
        saveChanges(oldReservation, reservationEdit.getOldDatetime(), reservationEdit.getOldMeetroomId());
        //check for the new users for the reservation
        List<Integer> usersIdsToAdd = userService.findDifferentUsers(newIdsArray, oldIdsArray);
        //add the users for the reservation in case of any
        if(usersIdsToAdd.size() > 0){
            addNewUsers(usersIdsToAdd, oldReservation);
        }
    }

    @Override
    public void deleteByDatetimeAndMeetroomId(String datetime, Integer meetroomId) {
        reservationRepository
                .deleteAll(reservationRepository
                        .findByDatetimeAndMeetroomId(LocalDateTime.parse(datetime), meetroomId));
    }

    public List<Reservation> findAllByOrderByDatetimeAscMeetroomIdAsc() {
        return reservationRepository.findAllByOrderByDatetimeAscMeetroomIdAsc();
    }

    public List<Reservation> findTotalReservations() {
        List<Reservation> oldReservs = reservationRepository.findAllByOrderByDatetimeAscMeetroomIdAsc();
        return oldReservs.stream().filter(distinctByKey(x->x.getDatetime())).collect(Collectors.toList());
    }

    public List<ReservationView> findReservationsWithUsers() {
        List<Reservation> originalReservations = findTotalReservations();
        List<ReservationView> reservations = new java.util.ArrayList<>(Collections.emptyList());
        for (Reservation r : originalReservations) {
            List<UserView> usersForOneReserv =
                    findByDatetimeAndMeetroomId(r.getDatetime().toString(), r.getMeetroom().getId())
                    .stream()
                    .map(x->new UserView(x.getUser().getId(), x.getUser().getEmail(), x.getUser().getLogin()))
                    .collect(Collectors.toList());

            MeetroomView meetroom = new MeetroomView(r.getMeetroom().getId(), r.getMeetroom().getName(), r.getMeetroom().getLocation());

            ReservationView reservationView = new ReservationView(r.getId(), r.getDatetime(),
                                                            r.getDuration(), usersForOneReserv, meetroom);
            reservations.add(reservationView);
        }
        return reservations;
    }
    public ReservationView findReservationsWithUsers(String datetime, Integer meetroomId) {
        Reservation reservation = findByDatetimeAndMeetroomId(datetime, meetroomId).get(0);
        ReservationView reservationView = new ReservationView();
        List<UserView> usersForOneReserv =
                    findByDatetimeAndMeetroomId(datetime, meetroomId)
                            .stream()
                            .map(x->new UserView(x.getUser().getId(), x.getUser().getEmail(), x.getUser().getLogin()))
                            .collect(Collectors.toList());
        MeetroomView meetroomViewModel = new MeetroomView(reservation.getMeetroom().getId(), reservation.getMeetroom().getName(),
                                            reservation.getMeetroom().getLocation());
        reservationView = new ReservationView(reservation.getId(), reservation.getDatetime(), reservation.getDuration(),
                                usersForOneReserv, meetroomViewModel);
        return reservationView;
    }

    public void saveChanges(ReservationView reservationView, String datetime, Integer meetroomId) {
        List<Reservation> oldReservations = findByDatetimeAndMeetroomId(datetime, meetroomId);
        for (Reservation r : oldReservations) {
            r.setMeetroom(meetroomService.findById(reservationView.getMeetroom().getId()));
            r.setDatetime(reservationView.getDatetime());
            r.setDuration(reservationView.getDuration());
            save(r);
        }
    }

    public void deleteOldUsers(List<Integer> usersIds, String datetime) {
        for (Integer id : usersIds) {
            reservationRepository.deleteByDatetimeAndUserId(LocalDateTime.parse(datetime), id);
        }
    }

    public void addNewUsers(List<Integer> usersIds, ReservationView reservationView) {
        List<User> users = userService.getUsersByIds(usersIds);
        for (User user : users) {
            Reservation reservation = new Reservation( reservationView.getDatetime(), reservationView.getDuration(),
                                        user, meetroomService.findById(reservationView.getMeetroom().getId()));
            save(reservation);
        }
    }

    public ReservationAddEdit getReservationForEdit(ReservationRequest reservation){
        ReservationView reservationFromDB =  findReservationsWithUsers(
                reservation.getDatetime(), reservation.getMeetroomId());
        String userIds = userService.getUserIdsFromReservation(reservationFromDB);
        ReservationAddEdit reservationToEdit = new ReservationAddEdit(userIds, reservation.getMeetroomId(),
                reservation.getDatetime(), reservationFromDB.getDuration().toString());
        return  reservationToEdit;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
