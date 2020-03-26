package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.repos.UserRepository;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.meetroomreservation.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplem implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> findAll(){
        return userRepository
                .findAll()
                .stream()
                .map(user -> new User(user.getId(), user.getEmail(), user.getLogin(), user.getPassword(), user.getRoles()))
                .collect(Collectors.toList());

    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void save(User user){
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
}
