package org.example.meetroomreservation.service;

import org.example.meetroomreservation.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findByEmail(String email);
    void save(User user);

}
