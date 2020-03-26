package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.Reservation;
import org.example.meetroomreservation.repos.ReservationRepository;
import org.example.meetroomreservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImplem implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    public void save(Reservation reservation){
        reservationRepository.save(reservation);
    }
}
