package org.example.meetroomreservation.repos;

import org.example.meetroomreservation.domain.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
}
