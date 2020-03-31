package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.domain.ReservationViewModel;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.repos.ReservationRepository;
import org.example.meetroomreservation.service.MeetroomService;
import org.example.meetroomreservation.service.ReservationService;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Reservation> findAll(){ return reservationRepository.findAll();}

    public void save(Reservation reservation){
        reservationRepository.save(reservation);
    }

    public List<Reservation> findByDatetimeAndMeetroomId(String datetime, Integer meetroomId) {
        return reservationRepository.findByDatetimeAndMeetroomId(LocalDateTime.parse(datetime), meetroomId);
    }

    @Override
    public void deleteByDatetimeAndMeetroomId(String datetime, Integer meetroomId) {
        reservationRepository
                .deleteAll(reservationRepository
                        .findByDatetimeAndMeetroomId
                        (LocalDateTime.parse(datetime), meetroomId));
    }

    public List<Reservation> findAllByOrderByDatetimeAscMeetroomIdAsc() {
        return reservationRepository.findAllByOrderByDatetimeAscMeetroomIdAsc();
    }

    public List<Reservation> findTotalReservations() {
        List<Reservation> oldReservs = reservationRepository.findAllByOrderByDatetimeAscMeetroomIdAsc();
        return oldReservs.stream().filter(distinctByKey(x->x.getDatetime())).collect(Collectors.toList());
    }

    public List<ReservationViewModel> findReservationsWithUsers(){
        List<Reservation> originalReservations = findTotalReservations();
        List<ReservationViewModel> reservations = new java.util.ArrayList<>(Collections.emptyList());
        for(Reservation r:originalReservations){
            List<User> usersForOneReserv =
                    findByDatetimeAndMeetroomId(r.getDatetime().toString(), r.getMeetroom().getId())
                    .stream()
                    .map(x->new User(x.getUser().getId(), x.getUser().getEmail()))
                    .collect(Collectors.toList());
            ReservationViewModel reservationViewModel = new ReservationViewModel(r.getId(), r.getDatetime(), r.getDuration(), usersForOneReserv, r.getMeetroom());
            reservations.add(reservationViewModel);
        }
        return reservations;
    }
    public ReservationViewModel findReservationsWithUsers(String datetime, Integer meetroom_id){
        Reservation reservation = findByDatetimeAndMeetroomId(datetime,meetroom_id).get(0);
        ReservationViewModel reservationViewModel = new ReservationViewModel();
        List<User> usersForOneReserv =
                    findByDatetimeAndMeetroomId(datetime, meetroom_id)
                            .stream()
                            .map(x->new User(x.getUser().getId(), x.getUser().getEmail()))
                            .collect(Collectors.toList());
           reservationViewModel = new ReservationViewModel(reservation.getId(), reservation.getDatetime(), reservation.getDuration(), usersForOneReserv, reservation.getMeetroom());
        return reservationViewModel;
    }

    public void saveChanges(ReservationViewModel reservationViewModel, String datetime, Integer meetroom_id){
        List<Reservation> old_reservations = findByDatetimeAndMeetroomId(
                datetime,meetroom_id);
        for(Reservation r:old_reservations){
            r.setMeetroom(reservationViewModel.getMeetroom());
            r.setDatetime(reservationViewModel.getDatetime());
            r.setDuration(reservationViewModel.getDuration());
            save(r);
        }
    }

    public void deleteOldUsers(List<Integer> user_ids, String dateTime){
        for(Integer id:user_ids)
          reservationRepository.deleteByDatetimeAndUserId(LocalDateTime.parse(dateTime), id);
    }

    public void addNewUsers(List<Integer> user_ids, ReservationViewModel reservationViewModel){
        List<User> users = userService.getUsersByIds(user_ids);
        for(User user:users){
            Reservation reservation = new Reservation( reservationViewModel.getDatetime(), reservationViewModel.getDuration(), user, reservationViewModel.getMeetroom());
            save(reservation);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
