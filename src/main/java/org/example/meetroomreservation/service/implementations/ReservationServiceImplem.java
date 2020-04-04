package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.*;
import org.example.meetroomreservation.domain.viewModels.MeetroomViewModel;
import org.example.meetroomreservation.domain.viewModels.ReservationViewModel;
import org.example.meetroomreservation.domain.viewModels.UserViewModel;
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

    public  void addReservation(String userId, Integer meetroomId,
                                String datetime, String duration) {
        if (userId.contains(";")) {
            int[] userIds = userService.updateStringIdsToIntArray(userId);
            for(Integer uId : userIds){
                Reservation reservation = new Reservation(LocalDateTime.parse(datetime), LocalTime.parse(duration),
                        userService.findById(uId),  meetroomService.findById(meetroomId));
                save(reservation);
            }
        } else {
            Reservation reservation = new Reservation(LocalDateTime.parse(datetime),LocalTime.parse(duration),
                    userService.findById(Integer.parseInt(userId)), meetroomService.findById(meetroomId));
            save(reservation);
        }

    }

    public void editReservation(String datetime, Integer meetroomId, String newDatetime,
                                 Integer newMeetroomId, String userIds, String duration) {
        //find old records about reservation
        ReservationViewModel oldReservation = findReservationsWithUsers(datetime, meetroomId);
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
            deleteOldUsers(usersIdsToDelete, datetime);
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
            Meetroom newMeetroom = meetroomService.findById(newMeetroomId);
            oldReservation.setMeetroom(new MeetroomViewModel(newMeetroom.getId(), newMeetroom.getName(), newMeetroom.getLocation()));
        }
        //save current reservation in DB
        saveChanges(oldReservation, datetime, meetroomId);
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

    public List<ReservationViewModel> findReservationsWithUsers() {
        List<Reservation> originalReservations = findTotalReservations();
        List<ReservationViewModel> reservations = new java.util.ArrayList<>(Collections.emptyList());
        for (Reservation r : originalReservations) {
            List<UserViewModel> usersForOneReserv =
                    findByDatetimeAndMeetroomId(r.getDatetime().toString(), r.getMeetroom().getId())
                    .stream()
                    .map(x->new UserViewModel(x.getUser().getId(), x.getUser().getEmail(), x.getUser().getLogin()))
                    .collect(Collectors.toList());

            MeetroomViewModel meetroom = new MeetroomViewModel(r.getMeetroom().getId(), r.getMeetroom().getName(), r.getMeetroom().getLocation());

            ReservationViewModel reservationViewModel = new ReservationViewModel(r.getId(), r.getDatetime(),
                                                            r.getDuration(), usersForOneReserv, meetroom);
            reservations.add(reservationViewModel);
        }
        return reservations;
    }
    public ReservationViewModel findReservationsWithUsers(String datetime, Integer meetroomId) {
        Reservation reservation = findByDatetimeAndMeetroomId(datetime, meetroomId).get(0);
        ReservationViewModel reservationViewModel = new ReservationViewModel();
        List<UserViewModel> usersForOneReserv =
                    findByDatetimeAndMeetroomId(datetime, meetroomId)
                            .stream()
                            .map(x->new UserViewModel(x.getUser().getId(), x.getUser().getEmail(), x.getUser().getLogin()))
                            .collect(Collectors.toList());
        MeetroomViewModel meetroomViewModel = new MeetroomViewModel(reservation.getMeetroom().getId(), reservation.getMeetroom().getName(),
                                            reservation.getMeetroom().getLocation());
        reservationViewModel = new ReservationViewModel(reservation.getId(), reservation.getDatetime(), reservation.getDuration(),
                                usersForOneReserv, meetroomViewModel);
        return reservationViewModel;
    }

    public void saveChanges(ReservationViewModel reservationViewModel, String datetime, Integer meetroomId) {
        List<Reservation> oldReservations = findByDatetimeAndMeetroomId(datetime, meetroomId);
        for (Reservation r : oldReservations) {
            r.setMeetroom(meetroomService.findById(reservationViewModel.getMeetroom().getId()));
            r.setDatetime(reservationViewModel.getDatetime());
            r.setDuration(reservationViewModel.getDuration());
            save(r);
        }
    }

    public void deleteOldUsers(List<Integer> usersIds, String datetime) {
        for (Integer id : usersIds) {
            reservationRepository.deleteByDatetimeAndUserId(LocalDateTime.parse(datetime), id);
        }
    }

    public void addNewUsers(List<Integer> usersIds, ReservationViewModel reservationViewModel) {
        List<User> users = userService.getUsersByIds(usersIds);
        for (User user : users) {
            Reservation reservation = new Reservation( reservationViewModel.getDatetime(), reservationViewModel.getDuration(),
                                        user, meetroomService.findById(reservationViewModel.getMeetroom().getId()));
            save(reservation);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
