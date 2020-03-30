package org.example.meetroomreservation.repos;

import org.example.meetroomreservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByDatetimeAndMeetroomId(LocalDateTime dateTime, Integer meetroom_id);
    List<Reservation> findAllByOrderByDatetimeAscMeetroomIdAsc();
    @Transactional
    void deleteByDatetimeAndUserId(LocalDateTime dateTime, Integer user_id);
}
