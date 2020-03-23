package org.example.meetroomreservation.repos;

import org.example.meetroomreservation.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
