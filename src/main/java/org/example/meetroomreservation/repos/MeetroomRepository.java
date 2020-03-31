package org.example.meetroomreservation.repos;

import org.example.meetroomreservation.domain.Meetroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetroomRepository extends JpaRepository<Meetroom, Integer> {
    List<Meetroom> findAllByOrderByIdAsc();
}
