package org.example.meetroomreservation.service.implementations;

import org.example.meetroomreservation.domain.viewModels.ReservationView;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.domain.viewModels.UserView;
import org.example.meetroomreservation.repos.UserRepository;
import org.example.meetroomreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplem implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findById(Integer id) { return userRepository.getOne(id);}

    public void save(User user){
        userRepository.save(user);
    }

    public String getUserIdsFromReservation(ReservationView reservationView) {
        String  userIds = "";
        for (UserView u : reservationView.getUsers()) {
            userIds += u.getId().toString() + ";";
        }
        return  userIds;
    }

    public List<Integer> findDifferentUsers(int[] oldIds, int[] newIds) {
        HashSet<Integer> s = new HashSet<>();

        for (int new_id : newIds) {
            s.add(new_id);
        }
        List<Integer> lostIds = new ArrayList<>();

        for (Integer oldId : oldIds)
            if (!s.contains(oldId))
                lostIds.add(oldId);
        return  lostIds;
    }

    public List<User> getUsersByIds(List<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    public int[] updateStringIdsToIntArray(String ids) {
        return Arrays.stream(ids.split(";")).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public List<User> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> new User(user.getId(), user.getEmail(), user.getLogin(), user.getPassword(), user.getRoles()))
                .collect(Collectors.toList());

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            return userRepository.findByEmail(email);
    }
}
