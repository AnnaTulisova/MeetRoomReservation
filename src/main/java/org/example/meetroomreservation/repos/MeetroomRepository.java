package org.example.meetroomreservation.repos;

import org.example.meetroomreservation.domain.Meetroom;
import org.springframework.data.repository.CrudRepository;

public interface MeetroomRepository extends CrudRepository<Meetroom, Integer> {
    Iterable<Meetroom> findAllByOrderByIdAsc();
}
